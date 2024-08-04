package com.boricori.dto;

import lombok.Data;

@Data
public class KakaoUserResponse {

  private Long id;

  private SocialProfile kakao_account;
}
