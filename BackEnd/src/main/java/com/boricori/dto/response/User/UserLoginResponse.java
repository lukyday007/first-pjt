package com.boricori.dto.response.User;

import com.boricori.util.ResponseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginResponse {

  @Schema(description = "유저 닉네임", example = "name123")
  String username;

  @Schema(description = "유저 이메일", example = "asdsasad@gmail.com")
  String email;

  @Schema(description = "액세스 토큰", example = "aaa.bbb.cccc")
  String accessToken;

  @Builder
  public UserLoginResponse (String username, String email, String accessToken){
    this.username = username;
    this.email = email;
    this.accessToken = accessToken;
  }
}
