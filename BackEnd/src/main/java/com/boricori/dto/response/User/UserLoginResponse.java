package com.boricori.dto.response.User;

import com.boricori.util.ResponseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginResponse {
  @Schema(description = "JWT 토큰, 기본적으로 요청시 유효성 검사할 때 사용", example = "aaaaa.bbbbbb.ccccccc")
  String accessToken;

  @Schema(description = "JWT 토큰, 만약 액세스 토큰이 만료되었을 시 다시 요청", example = "aaaaa.bbbbbb.ccccccc")
  String refreshToken;

  @Schema(description = "로그인 성공 여부", example = "SUCCESS")
  ResponseEnum result;

  public static UserLoginResponse of(String access, String refresh, ResponseEnum result){
    UserLoginResponse res = new UserLoginResponse();
    res.setAccessToken(access);
    res.setRefreshToken(refresh);
    res.setResult(result);
    return res;
  }
}
