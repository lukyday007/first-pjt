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
  public void makeRoom(StartGameRoomRequest gameRoomInfo) {
    GameRoom gameRoom = new GameRoom(gameRoomInfo);
    gameRoomRepository.save(gameRoom);
  }
}
