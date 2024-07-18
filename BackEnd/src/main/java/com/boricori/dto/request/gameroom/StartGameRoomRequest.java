package com.boricori.dto.request.gameroom;

import com.boricori.dto.request.gameroom.setting.GameSettingRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StartGameRoomRequest {

    @Schema(description = "게임방 세팅")
    private GameSettingRequest setting;

    @Schema(description = "게임시작 시간", example = "2024-07-17T15:00:00")
    private LocalDateTime startTime;

    @Schema(description = "방 활성화 여부", example = "true")
    private boolean isActivated; // 방 활성화

    @Schema(description = "입장 코드", example = "1234")
    private String codeNum; // 입장 코드

    @Schema(description = "게임방 사용자 목록")
    private List<PlayerInfoRequest> playerInfoRequests; // 사용자 목록
}
