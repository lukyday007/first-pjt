package com.boricori.dto.request.gameroom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EndGameRoomRequest {

    @Schema(description = "방 활성화 여부", example = "false")
    private boolean isActivated; // 방 활성화

    @Schema(description = "게임 종료 시간", example = "2024-07-17:15:10:00")
    private LocalDateTime endTime;

    @Schema(description = "살아남은 유저와 죽은 유저")
    private List<EndPlayerInfo> playerinfos;
}
