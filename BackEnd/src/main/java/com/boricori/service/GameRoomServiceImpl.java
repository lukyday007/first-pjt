package com.boricori.service;

import com.boricori.dto.request.gameroom.StartGameRoomRequest;
import com.boricori.entity.GameRoom;
import com.boricori.repository.GameRoomRepo.GameRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameRoomServiceImpl implements GameRoomService {

  @Autowired
  private GameRoomRepository gameRoomRepository;

  @Override
  public long makeRoom(StartGameRoomRequest gameRoomInfo) {
    GameRoom saveRoom = gameRoomRepository.save(new GameRoom(gameRoomInfo));
    return saveRoom.getId();
  }
}
