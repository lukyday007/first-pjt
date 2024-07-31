package com.boricori.service;

import com.boricori.dto.request.inGame.MissionChangeRequest;
import com.boricori.dto.request.inGame.UseItemRequest;
import com.boricori.entity.GameParticipants;
import com.boricori.entity.InGameItems;
import com.boricori.entity.InGameMissions;
import com.boricori.entity.Item;
import com.boricori.entity.Mission;
import com.boricori.repository.ParticipantRepo.ParticipantRepositoryImpl;
import com.boricori.repository.inGameRepo.InGameItemsRepository;
import com.boricori.repository.inGameRepo.InGameMissionsRepository;
import com.boricori.repository.inGameRepo.InGameRepositoryImpl;
import com.boricori.repository.inGameRepo.ItemRepository;
import com.boricori.repository.inGameRepo.ItemRepositoryImpl;
import com.boricori.repository.inGameRepo.MissionRepository;
import com.boricori.repository.inGameRepo.MissionRepositoryImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InGameServiceImpl implements InGameService{

  @Autowired
  private MissionRepository missionRepository;
  @Autowired
  private ItemRepository itemRepository;
  @Autowired
  private MissionRepositoryImpl missionRepositoryImpl;
  @Autowired
  private InGameRepositoryImpl inGameRepositoryImpl;
  @Autowired
  private ItemRepositoryImpl itemRepositoryImpl;
  @Autowired
  private ParticipantRepositoryImpl participantRepository;
  @Autowired
  private InGameMissionsRepository inGameMissionsRepository;
  @Autowired
  private InGameItemsRepository inGameItemsRepository;

  @Override
  public List<Mission> assignMissions(String username, Long gameId) {
    List<Mission> missions = missionRepositoryImpl.getMissions();
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    for (Mission m : missions){
      InGameMissions igm = InGameMissions.builder().missionId(m).user(player).build();
      inGameMissionsRepository.save(igm);
    }
    return missions;
  }

  @Override
  public Mission changeMission(Long gameId, String username, MissionChangeRequest request) {
    Mission newMission =  missionRepositoryImpl.changeMission(request.getMissionId());
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    inGameMissionsRepository.save(
        InGameMissions.builder().missionId(newMission).user(player).build()
    );
    inGameRepositoryImpl.updateMission(request.getMissionId(), player);
    return newMission;
  }

  @Override
  public void completeMission(Long gameId, String username, MissionChangeRequest request) {
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    inGameRepositoryImpl.updateMission(request.getMissionId(), player);
  }

  @Override
  public Item getItem(Long gameId, String username) {
    Item item = itemRepositoryImpl.getItem();
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    inGameItemsRepository.save(InGameItems.builder().item(item).user(player).build());
    return item;
  }

  @Override
  public void useItem(Long gameId, String username, UseItemRequest req) {
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    inGameRepositoryImpl.useItem(player, req.getItemId());
  }
}
