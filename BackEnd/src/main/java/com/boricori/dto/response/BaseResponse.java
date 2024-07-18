package com.boricori.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "BaseResponse")
public class BaseResponse {
  @Schema(name="응답 메시지", example = "정상")
  String message = null;
  @Schema(name="응답 코드", example = "200")
  Integer statusCode = null;

  public BaseResponse() {}

  public BaseResponse(Integer statusCode){
    this.statusCode = statusCode;
  }

  public BaseResponse(Integer statusCode, String message){
    this.statusCode = statusCode;
    this.message = message;
  }

  public static BaseResponse of(Integer statusCode, String message) {
    BaseResponse body = new BaseResponse();
    body.message = message;
    body.statusCode = statusCode;
    return body;
  }
}