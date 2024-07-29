package com.boricori.util;


import com.boricori.exception.NoSuchTokenException;
import com.boricori.exception.TokenExpiredException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;


public class JwtAuthenticationFilter implements Filter {

  @Autowired
  JwtUtil jwtUtil;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String authHeader = httpRequest.getHeader("Authorization");
    String refreshToken = httpRequest.getHeader("RefreshToken").substring(7);
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String accessToken = authHeader.substring(7);
      try {
        // JWT 유효성 검사 로직 구현
        if (isValid(accessToken, refreshToken)) {
          // 토큰이 유효한 경우 요청을 계속 처리
          String email = jwtUtil.getEmail(accessToken);
          httpRequest.setAttribute("email", email);
          chain.doFilter(request, response);
        } else {
          // access token 재발급
          String newToken = jwtUtil.createAccessToken(jwtUtil.getEmail(accessToken));
          httpResponse.addHeader("NewToken", newToken);
          httpResponse.sendError(ResponseEnum.TOKEN_RENEWED.getCode(), "토큰 재발급 완료");
        }
      } catch (NoSuchTokenException e) {
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
      } catch (TokenExpiredException e) {
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh Token Expired");
      } catch (Exception e) {
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
            "Error occurred while processing token...");
      }
    } else {
      // Authorization 헤더가 없거나 형식이 올바르지 않은 경우 오류 응답 반환
      httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
          "Authorization header missing or not Bearer");
    }
  }


  // JWT 토큰의 유효성을 검사하는 메서드
  private boolean isValid(String accessToken, String refreshToken) throws NoSuchTokenException {
    if (!jwtUtil.isExpired(accessToken)) {
      // access token이 유효한 상태
      return true;
    } else {
      if (!jwtUtil.isValidRefreshToken(refreshToken)) {
        throw new NoSuchTokenException();
      } else if (jwtUtil.isExpired(refreshToken)) {
        throw new TokenExpiredException();
      } else if (!jwtUtil.getEmail(accessToken).equals(jwtUtil.getEmail(refreshToken))) {
        throw new NoSuchTokenException();
      }
      // valid refresh token이며 access token 재발급 가능한 상태
      return false;
    }
  }
}
