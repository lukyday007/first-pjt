package com.boricori.service;

import com.boricori.dto.request.gameroom.PlayerInfoRequest;
import java.util.List;

public interface ParticipantsService {

  void makeGameParticipant(long id, List<PlayerInfoRequest> playerInfoRequests);

}
