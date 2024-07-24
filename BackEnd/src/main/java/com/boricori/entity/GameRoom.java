package com.boricori.entity;

import com.boricori.dto.request.gameroom.StartGameRoomRequest;
import com.boricori.dto.request.gameroom.setting.GameSettingRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class GameRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "game_id")
  private Long id;

  @Column(length = 64, nullable = false)
  private String roomName = "";

  @Column(nullable = false)
  private int maxPlayer;

  private boolean isActivated;

  @Column(nullable = false)
  private int mapSize;

  private int gameTime;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String codeNumber;

  @Builder
  public GameRoom(String roomName, int maxPlayer, int mapSize, String codeNumber) {
    this.roomName = roomName;
    this.maxPlayer = maxPlayer;
    this.isActivated = true;
    this.mapSize = mapSize;
    this.codeNumber = codeNumber;
  }

  public GameRoom(StartGameRoomRequest gameRoomRequest) {
    roomSetting(gameRoomRequest.getSetting());
    this.startTime = LocalDateTime.now();
    this.codeNumber = gameRoomRequest.getCodeNum();
  }

  private void roomSetting(GameSettingRequest setting) {
    this.roomName = setting.getName();
    this.maxPlayer = setting.getMaxPlayer();
    this.mapSize = setting.getMapSize();
    this.gameTime = setting.getTime();
  }

  public void updateGameRoom(GameSettingRequest request) {
    this.roomName = request.getName();
    this.maxPlayer = request.getMaxPlayer();
    this.mapSize = request.getMapSize();
  }
}
