package com.boricori.dto.response.gameroom;

import com.boricori.entity.GameRoom;
import com.boricori.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
public class GameInfoResponse {

  @Schema(description = "방 이름", example = "게임방1")
  private String name;

  @Schema(description = "게임 이용 시간", example = "10")
  private int time;

  @Schema(description = "맵 크기", example = "100")
  private int mapSize;

  @Schema(description = "시작 시간")
  private LocalDateTime startTime;

  @Schema(description = "중심 위도")
  private String centerLat;

  @Schema(description = "중심 경도")
  private String centerLng;

  @Schema(description = "내가 쫓는 사람의 닉네임")
  private String targetName;



  @Builder
  public GameInfoResponse(String name, String centerLng, String centerLat,
      LocalDateTime startTime, int mapSize, int time, String targetName) {
    this.name = name;
    this.centerLng = centerLng;
    this.centerLat = centerLat;
    this.startTime = startTime;
    this.mapSize = mapSize;
    this.time = time;
    this.targetName = targetName;
  }

  public static GameInfoResponse of(GameRoom gameRoom, User target) {
    return GameInfoResponse.builder()
        .name(gameRoom.getRoomName())
        .centerLat(gameRoom.getCenterLat())
        .centerLng(gameRoom.getCenterLng())
        .startTime(gameRoom.getStartTime())
        .mapSize(gameRoom.getMapSize())
        .time(gameRoom.getGameTime())
        .targetName(target.getUsername())
        .build();
  }
}
