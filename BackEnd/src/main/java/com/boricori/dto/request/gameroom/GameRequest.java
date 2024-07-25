package com.boricori.dto.request.gameroom;

import com.boricori.dto.request.gameroom.setting.GameSettingRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GameRequest {
    @Schema(description = "게임방 세팅")
    private GameSettingRequest setting;
}
