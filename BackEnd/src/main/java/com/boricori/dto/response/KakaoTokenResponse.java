package com.boricori.dto.response;

import lombok.Data;

@Data
public class KakaoTokenResponse {
  // 토큰 타입, bearer로 고정
  private String token_type;

//  사용자 액세스 토큰 값
  private String access_token;

//  ID 토큰 값
//  OpenID Connect 확장 기능을 통해 발급되는 ID 토큰, Base64 인코딩 된 사용자 인증 정보 포함
//  액세스 토큰과 ID 토큰의 만료 시간(초)
  private KakaoToken id_token;

//  액세스 토큰과 ID 토큰의 만료 시간(초)
  private int expires_in;

//  사용자 리프레시 토큰 값
  private String refresh_token;

//  리프레시 토큰 만료 시간(초)
  private int refresh_token_expires_in;

//  인증된 사용자의 정보 조회 권한 범위
//  범위가 여러 개일 경우, 공백으로 구분
  private String scope;
}
