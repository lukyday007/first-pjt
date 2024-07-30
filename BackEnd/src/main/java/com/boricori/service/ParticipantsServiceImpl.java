package com.boricori.service;

import com.boricori.dto.request.gameroom.PlayerInfoRequest;
import com.boricori.entity.GameParticipants;
import com.boricori.entity.GameRoom;
import com.boricori.entity.User;
import com.boricori.repository.ParticipantRepo.ParticipantRepository;
import com.boricori.repository.userRepo.UserRepositoryImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantsServiceImpl implements ParticipantsService {

  @Autowired
  private ParticipantRepository participantRepository;

  @Autowired
  private UserRepositoryImpl userRepositoryImpl;

  @Override
  public List<User> makeGameParticipant(GameRoom gameRoom, List<PlayerInfoRequest> playerInfoList) {

    List<User> gameUsers = userRepositoryImpl.getUserList(playerInfoList);
    gameUsers.forEach(user -> {
      participantRepository.save(new GameParticipants(gameRoom, user));
    });

    return gameUsers;

  }

  @Override
  public void addRecord(GameParticipants participants) {
    participantRepository.save(participants);
  }

}
