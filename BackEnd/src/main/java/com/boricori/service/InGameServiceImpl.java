package com.boricori.service;


import com.boricori.dto.ItemCount;
import com.boricori.dto.response.inGame.EndGameUserInfoResponse;
import com.boricori.entity.GameParticipants;
import com.boricori.entity.InGameItems;
import com.boricori.entity.InGameMissions;
import com.boricori.entity.Item;
import com.boricori.entity.Mission;
import com.boricori.entity.User;
import com.boricori.exception.NotAPlayerException;
import com.boricori.repository.GameRoomRepo.GameRoomRepository;
import com.boricori.repository.ParticipantRepo.ParticipantRepositoryImpl;
import com.boricori.repository.inGameRepo.InGameItemsRepository;
import com.boricori.repository.inGameRepo.InGameMissionsRepository;
import com.boricori.repository.inGameRepo.InGameRepositoryImpl;
import com.boricori.repository.inGameRepo.ItemRepository;
import com.boricori.repository.inGameRepo.ItemRepositoryImpl;
import com.boricori.repository.inGameRepo.MissionRepository;
import com.boricori.repository.inGameRepo.MissionRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  @Autowired
  private GameRoomRepository gameRoomRepository;
  @Autowired
  private RedisTemplate<String, String> redisTemplate;

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
  @Transactional
  public Mission changeMission(Long gameId, String username, long missionId) {
    Mission newMission =  missionRepositoryImpl.changeMission(missionId);
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    inGameMissionsRepository.save(
        InGameMissions.builder().missionId(newMission).user(player).build()
    );
    inGameRepositoryImpl.updateMission(missionId, player);
    return newMission;
  }

  @Override
  @Transactional
  public GameParticipants completeMission(Long gameId, String username, long missionId) {
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    player.getBullet();
    inGameRepositoryImpl.updateMission(missionId, player);
    return player;
  }

  @Override
  @Transactional
  public Item getItem(GameParticipants player) {
    Item item = itemRepositoryImpl.getItem();
    Optional<InGameItems> currCount = inGameItemsRepository.findByUserAndItem(player, item);
    if (currCount.isPresent()){
      currCount.get().incrementCount();
    }else{
      inGameItemsRepository.save(InGameItems.builder().item(item).user(player).count(1).build());
    }
    return item;
  }

  @Override
  @Transactional
  public void useItem(Long gameId, String username, long itemId) {
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    InGameItems item = inGameRepositoryImpl.getActivatedItem(player, itemId);
    if (null != item) {
      item.useItem();
    }
    // else 오류 처리 해야함
  }

  @Override
  public void catchTarget(User user, User target, long gameId) {
    GameParticipants hunterP = participantRepository.getByUsername(user.getUsername(), gameId);
    GameParticipants targetP = participantRepository.getByUsername(target.getUsername(), gameId);
    targetP.eliminate();
    hunterP.kill();
    hunterP.addBullets(targetP.getBullets());
    List<InGameItems> unusedItems = inGameRepositoryImpl.targetItemsCount(targetP);
    if (null != unusedItems && !unusedItems.isEmpty()){
      for (InGameItems igItem : unusedItems) {
        inGameRepositoryImpl.addItems(hunterP, igItem);
      }
    }
  }

  @Override
  public GameParticipants checkIfPlayer(String username, long gameId) {
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    if (player == null) {
      throw new NotAPlayerException();
    }
    return player;
  }

  @Override
  public List<Mission> getMissions(GameParticipants player) {
    return inGameRepositoryImpl.getMissions(player);
  }

  @Override
  public List<ItemCount> getPlayerItems(GameParticipants player){
    return inGameRepositoryImpl.getPlayersItems(player);
  }

  @Override
  public GameParticipants getUserInfo(Long gameId, String username){
      return participantRepository.getByUsername(username, gameId);
  }

  @Override
  public List<EndGameUserInfoResponse> getDrawEndGameUsersInfo(Long gameId, String usernameA, String usernameB) {
    return participantRepository.getDrawEndGameUsersInfo(gameId, usernameA, usernameB);
  }

  @Override
  public List<EndGameUserInfoResponse> getWinEndGameUsersInfo(Long gameId, String usernameA) {
    return participantRepository.getWinEndGameUsersInfo(gameId, usernameA);
  }

  @Override
  public void stopPlaying(String username, String roomId) {
    redisTemplate.opsForValue().set((username + "-" + roomId + "-left"), "leave", 60, TimeUnit.SECONDS);
  }

  @Override
  public void rejoin(String username, String roomId) {
    redisTemplate.opsForValue().getAndDelete((username + "-" + roomId + "-left"));
  }

  @Override
  public void killUser(String username, long roomId) {
    participantRepository.changeStatusByName(username, roomId);
  }

  @Override
  public Mission getMissionById(long missionId) {
    return missionRepository.findById(missionId).orElse(null);
  }
}
