package com.boricori.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "유저 정보 변경 폼")
public class UserUpdateRequest {

  @Schema(description = "프로필 사진 URL")
  String profilePic;

  @Schema(description = "유저 닉네임")
  String username;

  @Schema(description = "비밀번호")
  String password;

}
