package com.boricori.config;

import com.boricori.util.JwtAuthenticationFilter;
import com.boricori.util.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  private final JwtUtil jwtUtil;

  public FilterConfig(JwtUtil jwtUtil){
    this.jwtUtil = jwtUtil;
  }

  @Bean
  public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
    FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new JwtAuthenticationFilter(jwtUtil));
    registrationBean.addUrlPatterns("/in-game/*", "/gameroom/*"); // 필터를 적용할 URL 패턴 설정
    return registrationBean;
  }
}
