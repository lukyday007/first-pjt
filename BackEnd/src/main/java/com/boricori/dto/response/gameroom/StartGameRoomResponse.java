package com.boricori.dto.response.gameroom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class StartGameRoomResponse {
    @Schema(description = "게임방 설정정보")
    private GameRoomSettingResponse gameRoomSetting;

    @Schema(description = "게임 참여자 정보")
    private List<EnterUserInfo> playerInfo;

    @Schema(description = "미션 정보")
    private List<MissionResponse> missions;
}
