package com.boricori.service;

import com.boricori.dto.ParticipantNameDto;
import com.boricori.entity.GameRoom;
import java.util.List;

public interface GameService {

  public List<ParticipantNameDto> initCatchSequence();

}
