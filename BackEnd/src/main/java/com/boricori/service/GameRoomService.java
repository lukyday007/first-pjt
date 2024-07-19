package com.boricori.service;

import com.boricori.dto.request.gameroom.StartGameRoomRequest;

public interface GameRoomService {

  public void makeRoom(StartGameRoomRequest request);
}
