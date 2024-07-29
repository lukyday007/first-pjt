package com.boricori.dto.request.inGame;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MissionChangeRequest {
    @Schema(description = "미션 아이디")
    Long missionId;
}
