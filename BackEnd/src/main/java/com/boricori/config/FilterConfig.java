package com.boricori.config;

import com.boricori.util.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class FilterConfig {
  @Bean
  public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
    FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new JwtAuthenticationFilter());
//    registrationBean.addUrlPatterns("/api/*"); // 필터를 적용할 URL 패턴 설정
    return registrationBean;
  }
}
