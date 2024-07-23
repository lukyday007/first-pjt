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

  @Column(nullable = false)
  private boolean magneticField;

  private int gameTime;

  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String codeNumber;

  public GameRoom(GameRequest request){
    roomSetting(request.getSetting());
    this.isActivated = true;
    this.codeNumber = request.getCodeNum();
  }

  private void roomSetting(GameSettingRequest setting) {
    this.roomName = setting.getName();
    this.maxPlayer = setting.getMaxPlayer();
    this.mapSize = setting.getMapSize();
    this.gameTime = setting.getTime();
    this.magneticField = setting.isMagenticField();
  }

  public void updateGameRoom(GameSettingRequest request) {
    this.roomName = request.getName();
    this.maxPlayer = request.getMaxPlayer();
    this.mapSize = request.getMapSize();
    this.magneticField = request.isMagenticField();
  }
}
