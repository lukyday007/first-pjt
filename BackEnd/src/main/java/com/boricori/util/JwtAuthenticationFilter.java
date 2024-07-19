package com.boricori.util;


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
import org.springframework.web.servlet.HandlerInterceptor;

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
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String jwtToken = authHeader.substring(7);
      try {
        // JWT 유효성 검사 로직 구현
        if (isValid(jwtToken)) {
          // 토큰이 유효한 경우 요청을 계속 처리
          chain.doFilter(request, response);
        } else {
          // 유효하지 않은 토큰인 경우 오류 응답 반환
          httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
        }
      } catch (Exception e) {
        // 토큰 검사 중 오류 발생 시 오류 응답 반환
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error validating JWT Token");
      }
    } else {
      // Authorization 헤더가 없거나 형식이 올바르지 않은 경우 오류 응답 반환
      httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or not Bearer");
    }
  }


  // JWT 토큰의 유효성을 검사하는 메서드
  private boolean isValid(String jwtToken) {

    return true; // 토큰이 유효한 경우 true 반환
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
