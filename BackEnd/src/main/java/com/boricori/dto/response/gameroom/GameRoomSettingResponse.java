package com.boricori.dto.response.gameroom;

import com.boricori.entity.GameRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GameRoomSettingResponse {

    @Schema(description = "방 이름", example = "게임방1")
    private String name;

    @Schema(description = "인원 수", example = "5")
    private int limit;

    @Schema(description = "게임 이용 시간", example = "10")
    private int time;

    @Schema(description = "맵 크기", example = "100")
    private int map_size;

    @Schema(description = "자기장 여부", example = "1")
    private boolean magnetic_field;

    public GameRoomSettingResponse(GameRoom request) {
        this.name = request.getRoomName();
        this.limit = request.getMaxPlayer();
        this.time = request.getGameTime();
        this.map_size = request.getMapSize();
        this.magnetic_field = request.isMagneticField();
    }
}
