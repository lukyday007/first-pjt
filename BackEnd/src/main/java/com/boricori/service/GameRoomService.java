package com.boricori.service;

import com.boricori.dto.request.gameroom.StartGameRoomRequest;
import com.boricori.entity.GameRoom;

public interface GameRoomService {

  public GameRoom makeRoom(StartGameRoomRequest request);
}
