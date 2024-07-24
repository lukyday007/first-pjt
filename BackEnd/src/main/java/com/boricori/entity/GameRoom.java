package com.boricori.entity;

import com.boricori.dto.request.gameroom.GameRequest;
import com.boricori.dto.request.gameroom.setting.GameSettingRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

  @Column(length = 1024)
  private String qrCode;


  public void createQrCode(String qrCode){
    this.qrCode = qrCode;
  }

  public GameRoom(GameRequest gameRoomRequest) {
    roomSetting(gameRoomRequest.getSetting());
    isActivated = true;
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
