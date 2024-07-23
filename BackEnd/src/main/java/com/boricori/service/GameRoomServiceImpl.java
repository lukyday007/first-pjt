package com.boricori.service;

import com.boricori.dto.request.gameroom.GameRequest;
import com.boricori.dto.response.gameroom.GameRoomSettingResponse;
import com.boricori.entity.GameRoom;
import com.boricori.repository.GameRoomRepo.GameRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameRoomServiceImpl implements GameRoomService {

  @Autowired
  private GameRoomRepository gameRoomRepository;

  @Override
  @Transactional
  public GameRoomSettingResponse makeRoom(GameRequest gameRoomInfo) {
    GameRoom save = gameRoomRepository.save(new GameRoom(gameRoomInfo));
    return new GameRoomSettingResponse(save);
  }
}
