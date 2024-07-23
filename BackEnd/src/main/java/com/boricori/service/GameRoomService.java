package com.boricori.service;

import com.boricori.dto.request.gameroom.GameRequest;
import com.boricori.dto.response.gameroom.GameRoomSettingResponse;

public interface GameRoomService {

  public GameRoomSettingResponse makeRoom(GameRequest request);
}
