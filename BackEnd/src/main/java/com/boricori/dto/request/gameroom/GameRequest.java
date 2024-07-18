package com.boricori.dto.request.gameroom;

import com.boricori.dto.request.gameroom.setting.GameSettingRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GameRequest {
    @Schema(description = "게임방 세팅")
    private GameSettingRequest setting;

    @Schema(description = "게임방 코드 번호", example = "1234")
    private String codeNum;

    @Schema(description = "게임방 활성화 여부", example = "true")
    private boolean isActivated;
}
