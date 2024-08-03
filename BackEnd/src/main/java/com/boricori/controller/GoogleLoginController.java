package com.boricori.controller;

import com.boricori.dto.KakaoAccount;
import com.boricori.dto.KakaoUserResponse;
import com.boricori.dto.response.AuthToken;
import com.boricori.entity.User;
import com.boricori.service.UserService;
import com.boricori.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth/google")
public class GoogleLoginController {

  @Value("${google.redirect_url}")
  private String REDIRECT_URL;
  @Value("${google.client_id}")
  private String CLIENT_ID;
  @Value("${google.client_secret}")
  private String CLIENT_SECRET;

  @Autowired
  private JwtUtil jwtUtil;

  private String scope = "https://www.googleapis.com/auth/userinfo.email";

  private static RestTemplate restTemplate = new RestTemplate();
  @Autowired
  private UserService userService;

  @GetMapping("/getURL")
  public ResponseEntity<String> getURL(HttpServletRequest request){
    // Create a state token to prevent request forgery.
    // Store it in the session for later validation.
    HttpSession session = request.getSession();
    String state = new BigInteger(130, new SecureRandom()).toString(32);
    session.setAttribute("state", state);

    String url = "https://accounts.google.com/o/oauth2/v2/auth?"
        + "scope=" + scope + "&"
        + "access_type=offline&"
        + "include_granted_scopes=true&"
        + "response_type=code&"
        + "state=" + state + "&"
        + "redirect_uri=" + REDIRECT_URL + "&"
        + "client_id=" + CLIENT_ID;
    return ResponseEntity.status(200).body(url);
  }

  @GetMapping("/getCode")
  public void getCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String code = req.getParameter("code");
    String error = req.getParameter("error");
    String state = req.getParameter("state");

    if (error != null){
//      return "에러남: " + error; // 에ㅔ러값 리턴해서 콘솔에 찍어주자
    }

    HttpSession session = req.getSession();
    if (!session.getAttribute("state").equals(state)){
//      return "state 안맞음, expected: " + session.getAttribute("state") + ", actual: " + state; // 누군가 탈취한 정보로 접근중
    }

    String requestURL = "https://oauth2.googleapis.com/token";

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("client_id", CLIENT_ID);
    formData.add("client_secret", CLIENT_SECRET);
    formData.add("code", code);
    formData.add("grant_type", "authorization_code");
    formData.add("redirect_uri", REDIRECT_URL);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/x-www-form-urlencoded");
    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

    ResponseEntity<AuthToken> responseEntity = restTemplate.exchange(
        requestURL,
        HttpMethod.POST,
        entity,
        AuthToken.class
    );

    AuthToken responseBody = responseEntity.getBody();
    try{
      String email = getGoogleEmail(responseBody.getAccess_token());
      User user = userService.findByEmail(email);
      if (user == null){
        // 새로운 유저
        Cookie emailCookie = new Cookie("email", email);
        emailCookie.setHttpOnly(true); // Prevent JavaScript access
        emailCookie.setSecure(true);   // Only sent over HTTPS
        emailCookie.setPath("/");      // Valid for the entire application
        emailCookie.setMaxAge(60);   // 1 min expiration
        resp.addCookie(emailCookie);
        // Redirect to the frontend
        resp.sendRedirect("https://your-frontend.com/home"); // 닉네임 생성페이지로
      }else{
        Cookie accessCookie = new Cookie("access", jwtUtil.createAccessToken(user.getUsername()));
        Cookie refreshCookie = new Cookie("refresh", jwtUtil.createRefreshToken(user.getUsername()));
        accessCookie.setHttpOnly(true); // Prevent JavaScript access
        accessCookie.setSecure(true);   // Only sent over HTTPS
        accessCookie.setPath("/");      // Valid for the entire application
        accessCookie.setMaxAge(60);   // 1 min expiration
        resp.addCookie(accessCookie);
        resp.addCookie(refreshCookie);
        // Redirect to the frontend
        resp.sendRedirect("https://your-frontend.com/home"); // 닉네임 생성페이지로
      }
    }catch(Exception e){
        resp.sendRedirect(""); // 로그인페이지로
    }

  }


  public String getGoogleEmail(String token) throws RuntimeException{
    String url = "https://www.googleapis.com/oauth2/v2/userinfo";

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", String.format("Bearer %s", token));

    HttpEntity<String> entity = new HttpEntity<>(headers);

      ResponseEntity<KakaoAccount> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
          entity, KakaoAccount.class);
      if (responseEntity.getBody() == null) {
        throw new RuntimeException();
      }
      return responseEntity.getBody().getEmail();
  }

}
