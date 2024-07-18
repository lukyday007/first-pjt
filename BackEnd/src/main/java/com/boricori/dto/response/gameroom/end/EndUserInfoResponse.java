package com.boricori.dto.response.gameroom.end;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EndUserInfoResponse {

    @Schema(description = "플레이어 이름", example = "member1")
    public String username;

    @Schema(description = "플레이어 이메일", example = "xxx@gmail.com")
    public String email;

    @Schema(description = "총알 수", example = "10")
    public int bullet;

    @Schema(description = "죽인 수", example = "3")
    public int kills;

    @Schema(description = "미션 수행 수", example = "7")
    public int completeMission;
}
