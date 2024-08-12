package com.boricori.dto.request.User;

import lombok.Data;

@Data
public class SocialLoginRequest {

  private String code;

  private String state;

}
