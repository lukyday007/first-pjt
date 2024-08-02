package com.boricori.controller;

import com.boricori.dto.KakaoCode;
import com.boricori.dto.KakaoUserResponse;
import com.boricori.dto.request.KakaoTokenRequest;
import com.boricori.dto.request.User.UserSignupRequest;
import com.boricori.dto.response.KakaoTokenResponse;
import com.boricori.dto.response.User.UserLoginResponse;
import com.boricori.dto.response.User.UserResponse;
import com.boricori.entity.User;
import com.boricori.service.UserService;
import com.boricori.util.JwtUtil;
import com.boricori.util.ResponseEnum;
import jakarta.servlet.http.HttpServletRequest;
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

@RequestMapping("/kakao")
@RestController
public class SocialLoginController {

  @Value("${kakao.REST_API_KEY}")
  private String apiKey;

  @Value("${kakao.CLIENT_SECRET}")
  private String clientSecret;

  private String redirectUri = "http://localhost:5080/kakao";

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserService userService;

  private static RestTemplate restTemplate = new RestTemplate();

  @GetMapping("/login")
  public ResponseEntity<?> getToken(@RequestBody KakaoCode kakaocode) {

      String code = kakaocode.getCode();
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

      ResponseEntity<KakaoTokenResponse> responseEntity = restTemplate.exchange(
          url,
          HttpMethod.POST,
          entity,
          KakaoTokenResponse.class
      );

      KakaoTokenResponse responseBody = responseEntity.getBody();
      if (responseBody != null) {
        String token = responseBody.getAccess_token();
        String email = getKakaoEmail(token);
        if (!email.equals("ERROR")){
          return checkIfUser(email);
        }
      }
    return ResponseEntity.status(ResponseEnum.FAIL.getCode()).body("Error retrieving token");
  }

  public String getKakaoEmail(String token){
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
      return "ERROR";
    }
    return responseEntity.getBody().getKakao_account().getEmail();
  }

  public ResponseEntity<UserLoginResponse> checkIfUser(String email){
    User user = userService.findByEmail(email);
    if (user == null){
      // 유저가 등록되지 않은 상태
      return ResponseEntity.status(ResponseEnum.SIGNUP.getCode()).body(UserLoginResponse.builder()
          .email(email)
          .result(ResponseEnum.SIGNUP)
          .build()); // 닉네임 설정 페이지로 이동
    }
    return ResponseEntity.status(200).body(UserLoginResponse.builder()
        .email(user.getEmail())
        .username(user.getUsername())
        .access(jwtUtil.createAccessToken(user.getUsername()))
        .refresh(jwtUtil.createRefreshToken(user.getUsername()))
        .result(ResponseEnum.SUCCESS)
        .build());
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
