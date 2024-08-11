package com.boricori.service;


import com.boricori.dto.GameResult;
import com.boricori.dto.request.inGame.UpdatePlayerScoreRequest;
import com.boricori.dto.response.inGame.EndGameUserInfoResponse;
import com.boricori.entity.*;
import com.boricori.exception.NotAPlayerException;
import com.boricori.game.GameManager;
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
import java.util.concurrent.TimeUnit;

import com.boricori.repository.userRepo.UserRepositoryImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
  @Autowired
  private GameRoomRepository gameRoomRepository;
  @Autowired
  private UserRepositoryImpl userRepository;
  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  private GameManager gameManager = GameManager.getGameManager();

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
  public void completeMission(Long gameId, String username, long missionId) {
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    inGameRepositoryImpl.updateMission(missionId, player);
  }

  @Override
  public Item getItem(Long gameId, String username) {
    Item item = itemRepositoryImpl.getItem();
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    inGameItemsRepository.save(InGameItems.builder().item(item).user(player).build());
    return item;
  }

  @Override
  public void useItem(Long gameId, String username, long itemId) {
    GameParticipants player = participantRepository.getByUsername(username, gameId);
    inGameRepositoryImpl.useItem(player, itemId);
  }

  @Override
  public void catchTarget(User user, User target, long gameId) {
    participantRepository.changeStatus(target, gameId);
    participantRepository.addKills(user);
    GameParticipants participant = participantRepository.getByUsername(user.getUsername(), gameId);
    List<Item> unusedItems = inGameRepositoryImpl.getUserItems(target);
    List<InGameItems> igItems = new ArrayList<>();
    unusedItems.forEach(item -> igItems.add(new InGameItems(participant, item)));
    inGameItemsRepository.saveAll(igItems);
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
  public List<Item> getItems(GameParticipants player) {
    return inGameRepositoryImpl.getItems(player);
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

  @Override
  public void addGamePlayerScore(long gameId) {
    List<UpdatePlayerScoreRequest> gamePlayersInfo = participantRepository.getByPlayersInfo(gameId);

    // 첫 번째 플레이어의 정보 (최고 득점자)
    UpdatePlayerScoreRequest firstPlayer = gamePlayersInfo.get(0);

    for (UpdatePlayerScoreRequest player : gamePlayersInfo) {
      int score = calculatePlayerScore(player);

      // 1등 보너스 추가, 무승부
      if (isFirstPlace(player, firstPlayer, gamePlayersInfo.indexOf(player))) {
        score += 2000;
      }

      // 점수 업데이트
      userRepository.addUserScore(player.getUserId(), score);
      participantRepository.updateUserScore(player.getUserId(), score);
    }
  }

  private int calculatePlayerScore(UpdatePlayerScoreRequest player) {
    //킬 : 100점, 미션 : 50
    return player.getKills() * 100 + player.getMissionComplete() * 50;
  }

  private boolean isFirstPlace(UpdatePlayerScoreRequest player, UpdatePlayerScoreRequest firstPlayer, int index) {
    if (index == 0 && firstPlayer != null) {
      return true; // 첫 번째 플레이어
    } else if (index == 1) {
      // 2번째 플레이어가 1등과 동점인지 확인
      return player.getKills() == firstPlayer.getKills() &&
              player.getMissionComplete() == firstPlayer.getMissionComplete();
    }
    return false;
  }

  @Override
  public GameResult finishGameAndHandleLastTwoPlayers(long gameId){
    finishGame(gameId);
    List<String> users = gameManager.EndGameUserInfo(gameId);
    GameParticipants userA = participantRepository.getByUsername(users.get(0), gameId);
    GameParticipants userB = participantRepository.getByUsername(users.get(1), gameId);
    String winner = determineWinner(userA, userB);
    String winner2 = null;
    if (winner == null){
      winner = userA.getUser().getUsername();
      winner2 = userB.getUser().getUsername();
    }
    List<EndGameUserInfoResponse> usersInfo = participantRepository.getEndGamePlayersInfo(gameId);
    return new GameResult(gameId, winner, winner2, usersInfo);
  }



  private String determineWinner(GameParticipants userA, GameParticipants userB) {
    if (userA.getKills() == userB.getKills()) {
      if (userA.getMissionComplete() > userB.getMissionComplete()) {
        return userA.getUser().getUsername();
      } else if (userA.getMissionComplete() < userB.getMissionComplete()) {
        return userB.getUser().getUsername();
      } else {
        return null; // 무승부
      }
    } else {
      return userA.getKills() > userB.getKills() ? userA.getUser().getUsername() : userB.getUser().getUsername();
    }
  }

  // redis expired = 4 일 때, 타임아웃 종료
  @Override
  public GameResult gameTimeout(long gameId){
    finishGame(gameId);
    List<EndGameUserInfoResponse> res = participantRepository.getEndGamePlayersInfo(gameId);
    return new GameResult(gameId, null, null, res);
  }

  @Override
  @Transactional
  public void finishGame(long gameId){
    gameRoomRepository.findById(gameId).ifPresent(GameRoom::finish);
  }
}
