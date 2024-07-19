package com.boricori.service;

import com.boricori.dto.request.gameroom.StartGameRoomRequest;

public interface GameRoomService {

  public long makeRoom(StartGameRoomRequest request);
}
