package com.boricori.service;

import com.boricori.dto.request.gameroom.PlayerInfoRequest;
import com.boricori.repository.ParticipantRepo.ParticipantRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantsServiceImpl implements ParticipantsService {

  @Autowired
  private ParticipantRepository participantRepository;

  @Autowired
  private ParticipantsServiceImpl participantsServiceImpl;

  @Override
  public void makeGameParticipant(long id, List<PlayerInfoRequest> playerInfoList) {

//    GameParticipants gameParticipants = new GameParticipants();

//    GameParticipants save = participantRepository.save(gameParticipants);

    playerInfoList.forEach(playerInfo -> {
      String username = playerInfo.getUsername();
      String email = playerInfo.getEmail();
//      participantRepository
    });

  }

}
