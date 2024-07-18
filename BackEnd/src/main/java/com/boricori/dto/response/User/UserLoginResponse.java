package com.boricori.dto.response.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginResponse extends BaseResponse {
  @Schema(description = "JWT 토큰", example = "aaaaa.bbbbbb.ccccccc")
  String authToken;

  public static UserLoginResponse of(int code, String msg, String authToken){
    UserLoginResponse res = new UserLoginResponse();
    res.setStatusCode(code);
    res.setMessage(msg);
    res.setAuthToken(authToken);
    return res;
  }
}
