package com.boricori.dto.request.gameroom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EndPlayerInfoRequest {

    @Schema(description = "유저 이름", example = "member1")
    private String username;

    @Schema(description = "유저 이메일", example = "xxx@gmail.com")
    private String email;

    @Schema(description = "생존 여부", example = "true")
    private boolean isAlive;
}
