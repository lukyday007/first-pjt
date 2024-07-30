package com.boricori.dto.response.gameroom;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class JoinGameRoomResponse {

  @Schema(description = "게임방 설정정보")
  private GameRoomSettingResponse gameRoomSetting;

  @Schema(description = "게임 참여자 정보")
  private List<EnterUserInfoResponse> playerInfo;

  @Schema(description = "미션 정보")
  private List<MissionResponse> missions;

  public JoinGameRoomResponse() {
  }
}
