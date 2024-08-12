package com.boricori.dto;

public class KakaoToken extends AuthToken {


  //  리프레시 토큰 만료 시간(초)
  private int refresh_token_expires_in;


  public int getRefresh_token_expires_in() {
    return refresh_token_expires_in;
  }

  public void setRefresh_token_expires_in(int refresh_token_expires_in) {
    this.refresh_token_expires_in = refresh_token_expires_in;
  }
}
