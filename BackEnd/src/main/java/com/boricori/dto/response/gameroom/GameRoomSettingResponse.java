package com.boricori.dto.response.gameroom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GameRoomSettingResponse {

    @Schema(description = "방 이름", example = "게임방1")
    private String name;

    @Schema(description = "인원 수", example = "5")
    private int limit;

    @Schema(description = "게임 이용 시간", example = "10")
    private String time;

    @Schema(description = "맵 크기", example = "100")
    private int map_size;

    @Schema(description = "자기장 여부", example = "1")
    private int magnetic_field;
}
