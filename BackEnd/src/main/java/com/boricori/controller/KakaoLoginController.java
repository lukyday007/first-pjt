package com.boricori.controller;

import com.boricori.dto.KakaoCode;
import com.boricori.dto.KakaoToken;
import com.boricori.dto.KakaoUserResponse;
import com.boricori.dto.request.KakaoTokenRequest;
import com.boricori.dto.request.User.UserSignupRequest;
import com.boricori.dto.response.User.UserLoginResponse;
import com.boricori.dto.response.User.UserResponse;
import com.boricori.entity.User;
import com.boricori.service.UserService;
import com.boricori.util.JwtUtil;
import com.boricori.util.ResponseEnum;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/auth/kakao")
@RestController
public class KakaoLoginController {

  @Value("${kakao.REST_API_KEY}")
  private String apiKey;

  @Value("${kakao.CLIENT_SECRET}")
  private String clientSecret;

  private String redirectUri = "http://localhost:5080/auth/kakao";

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserService userService;

  private static RestTemplate restTemplate = new RestTemplate();


  @GetMapping("getURL")
  public ResponseEntity<String> getURL(HttpServletRequest request){
    // Create a state token to prevent request forgery.
// Store it in the session for later validation.
    HttpSession session = request.getSession();
    String state = new BigInteger(130, new SecureRandom()).toString(32);
    session.setAttribute("state", state);

    String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&"
        + "client_id=" + apiKey + "&"
        + "redirect_uri=" + redirectUri +"&"
        + "state=" + state;
    return ResponseEntity.status(200).body(url);
  }


  @PostMapping("/login")
  public ResponseEntity<?> getToken(@RequestBody KakaoCode kakaocode, HttpServletRequest request, HttpServletResponse resp)
      throws IOException {

      HttpSession session = request.getSession();
      String code = kakaocode.getCode();
      String state = kakaocode.getState();
      if (!session.getAttribute("state").equals(state)){
        return ResponseEntity.status(ResponseEnum.NOT_FOUND.getCode()).body("인증되지 않은 유저입니다.");
      }

      String url = "https://kauth.kakao.com/oauth/token";

      MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
      formData.add("grant_type", "authorization_code");
      formData.add("client_id", apiKey);
      formData.add("redirect_uri", redirectUri);
      formData.add("code", code);
      formData.add("client_secret", clientSecret);

      HttpHeaders headers = new HttpHeaders();
      headers.set("Content-Type", "application/x-www-form-urlencoded");
      HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

      ResponseEntity<KakaoToken> responseEntity = restTemplate.exchange(
          url,
          HttpMethod.POST,
          entity,
          KakaoToken.class
      );

    KakaoToken responseBody = responseEntity.getBody();
      if (responseBody != null) {
        String token = responseBody.getAccess_token();
        String email = getKakaoEmail(token);
        User user = userService.findByEmail(email);
        if (user == null) {
          // 새로운 유저
          Cookie emailCookie = new Cookie("email", email);
          emailCookie.setHttpOnly(true); // Prevent JavaScript access
          emailCookie.setSecure(true);   // Only sent over HTTPS
          emailCookie.setPath("/");      // Valid for the entire application
          emailCookie.setMaxAge(60);   // 1 min expiration
          resp.addCookie(emailCookie);
          // Redirect to the frontend
          resp.sendRedirect("https://your-frontend.com/home"); // 닉네임 생성페이지로
        } else {
          Cookie accessCookie = new Cookie("access", jwtUtil.createAccessToken(user.getUsername()));
          Cookie refreshCookie = new Cookie("refresh",
              jwtUtil.createRefreshToken(user.getUsername()));
          accessCookie.setHttpOnly(true); // Prevent JavaScript access
          accessCookie.setSecure(true);   // Only sent over HTTPS
          accessCookie.setPath("/");      // Valid for the entire application
          accessCookie.setMaxAge(60);   // 1 min expiration
          resp.addCookie(accessCookie);
          resp.addCookie(refreshCookie);
          // Redirect to the frontend
          resp.sendRedirect("https://your-frontend.com/home"); // 닉네임 생성페이지로
        }
      }
    return ResponseEntity.status(ResponseEnum.FAIL.getCode()).body("Error retrieving token");
  }

  public String getKakaoEmail(String token) throws RuntimeException{
    String url = "https://kapi.kakao.com/v2/user/me";

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("property_keys", "[\"kakao_account.email\"]");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    headers.set("Authorization", String.format("Bearer %s", token));
    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

    ResponseEntity<KakaoUserResponse> responseEntity = restTemplate.exchange(
        url,
        HttpMethod.POST,
        entity,
        KakaoUserResponse.class
    );
    if (responseEntity.getBody() == null){
      throw new RuntimeException();
    }
    return responseEntity.getBody().getKakao_account().getEmail();
  }


  @PostMapping("/signup")
  public ResponseEntity<?> kakaoSignup(@RequestBody UserSignupRequest request){
    request.setPassword("dbGP1ylSJU");
    User user = userService.signup(request);
    if (user != null){
      return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(UserResponse.of(user));
    }
    return ResponseEntity.status(ResponseEnum.FAIL.getCode()).body(null);
  }
}
