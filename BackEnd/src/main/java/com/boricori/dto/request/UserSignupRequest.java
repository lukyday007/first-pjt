package com.boricori.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "유저 회원가입 폼")
public class UserSignupRequest {
  @Schema(description = "유저 아이디", example = "example@gmail.com")
  String email;
  @Schema(description = "비밀번호", example = "myPassword")
  String password;

}
