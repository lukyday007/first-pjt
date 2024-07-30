package com.boricori.dto.request.gameroom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StartGameRoomRequest {
    
    @Schema(description = "게임 맵 중심 위도")
    private String centerLat;
    
    @Schema(description = "게임 맵 중심 경도")
    private String centerLng;
}
