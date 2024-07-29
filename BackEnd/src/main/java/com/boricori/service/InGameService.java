package com.boricori.service;

import com.boricori.dto.request.inGame.MissionChangeRequest;
import com.boricori.dto.request.inGame.UseItemRequest;
import com.boricori.entity.Item;
import com.boricori.entity.Mission;
import java.util.List;

public interface InGameService {

  List<Mission> assignMissions(String email, Long gameId);

  Mission changeMission(Long gameId, String email, MissionChangeRequest request);

  void completeMission(Long roomId, String email, MissionChangeRequest request);

  Item getItem(Long gameId, String email);

  void useItem(Long roomId, String email, UseItemRequest req);
}
