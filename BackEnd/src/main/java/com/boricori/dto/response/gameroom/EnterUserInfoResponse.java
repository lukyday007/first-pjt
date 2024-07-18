package com.boricori.dto.response.gameroom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EnterUserInfoResponse {

    @Schema(description = "사용자 이름", example = "member1")
    private String name;

    @Schema(description = "사용자 이메일", example = "xxx@gmail.com")
    private String email;
}
