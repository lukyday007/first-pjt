package com.boricori.entity;

import com.boricori.dto.request.gameroom.setting.GameSettingRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class GameRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="game_id")
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

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String codeNumber;

    @Builder
    public GameRoom(String roomName, int maxPlayer, int mapSize, boolean magneticField, String codeNumber) {
        this.roomName = roomName;
        this.maxPlayer = maxPlayer;
        this.isActivated = true;
        this.mapSize = mapSize;
        this.magneticField = magneticField;
        this.codeNumber = codeNumber;
    }

    public void updateGameRoom(GameSettingRequest request) {
        this.roomName = request.getName();
        this.maxPlayer = request.getMaxPlayer();
        this.mapSize = request.getMapSize();
        this.magneticField = request.isMagenticField();
    }
}
