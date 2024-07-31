package com.boricori.service;

import com.boricori.dto.request.inGame.MissionChangeRequest;
import com.boricori.dto.request.inGame.UseItemRequest;
import com.boricori.entity.Item;
import com.boricori.entity.Mission;
import java.util.List;

public interface InGameService {

  List<Mission> assignMissions(String username, Long gameId);

  Mission changeMission(Long gameId, String username, MissionChangeRequest request);

  void completeMission(Long roomId, String username, MissionChangeRequest request);

  Item getItem(Long gameId, String username);

  void useItem(Long roomId, String username, UseItemRequest req);
}
