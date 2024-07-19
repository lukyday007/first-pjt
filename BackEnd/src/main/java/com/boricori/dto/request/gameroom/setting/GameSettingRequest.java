package com.boricori.dto.request.gameroom.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GameSettingRequest {

    @Schema(description = "게임방 이름", example = "gameRoom1")
    private String name;

    @Schema(description = "게임방 인원 제한", example = "10")
    private int maxPlayer;

    @Schema(description = "게임 시간", example = "10")
    private int time; // 게임 시간

    @Schema(description = "맵 크기", example = "100")
    private int mapSize;

    @Schema(description = "자기장 활성화 여부", example = "true")
    private boolean magenticField;
}
