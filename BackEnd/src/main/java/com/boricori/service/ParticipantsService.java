package com.boricori.service;

import com.boricori.dto.request.gameroom.PlayerInfoRequest;
import com.boricori.entity.GameRoom;
import java.util.List;

public interface ParticipantsService {

  void makeGameParticipant(GameRoom gameRoom, List<PlayerInfoRequest> playerInfoRequests);

}
