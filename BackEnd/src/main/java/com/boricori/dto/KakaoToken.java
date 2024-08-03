package com.boricori.dto;

import com.boricori.dto.response.AuthToken;
import lombok.Data;

public class KakaoToken extends AuthToken {

  //  ID 토큰 값
//  OpenID Connect 확장 기능을 통해 발급되는 ID 토큰, Base64 인코딩 된 사용자 인증 정보 포함
//  액세스 토큰과 ID 토큰의 만료 시간(초)
  private com.boricori.dto.response.KakaoToken id_token;


  //  리프레시 토큰 만료 시간(초)
  private int refresh_token_expires_in;

  public com.boricori.dto.response.KakaoToken getId_token() {
    return id_token;
  }

  public void setId_token(com.boricori.dto.response.KakaoToken id_token) {
    this.id_token = id_token;
  }

  public int getRefresh_token_expires_in() {
    return refresh_token_expires_in;
  }

  public void setRefresh_token_expires_in(int refresh_token_expires_in) {
    this.refresh_token_expires_in = refresh_token_expires_in;
  }
}
