package com.boricori.service;

import com.boricori.dto.GameResult;
import com.boricori.dto.response.gameroom.EnterMessageResponse;
import com.boricori.entity.GameRoom;
import com.boricori.game.GameManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  private final SimpMessagingTemplate messagingTemplate;

  private final GameManager gameManager = GameManager.getGameManager();

  private static final ObjectMapper mapper = new ObjectMapper();

  public MessageService(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void readyGame(Long gameRoomId, GameRoom gameRoom){
    messagingTemplate.convertAndSend(String.format("/topic/waiting/%d", gameRoomId),"{\"msgType\":\"ready\"}");
  }

  public void startGame(Long id) {
    messagingTemplate.convertAndSend(String.format("/topic/waiting/%d", id), "{\"msgType\":\"start\"}");
  }

  public void processAlertMessage(String gameId, String alertJSON) {
    messagingTemplate.convertAndSend(String.format("/topic/play/%s", gameId), alertJSON);
  }

  public void changeTarget(String username, String newTarget, long gameId){
    String jsonPayload = String.format("{\"msgType\":\"changeTarget\", \"hunter\":\"%s\", \"target\":\"%s\"}", username, newTarget);
    messagingTemplate.convertAndSend(String.format("/topic/play/%d", gameId), jsonPayload);
  }

  public void notifyEliminated(String username, long gameId) {
    String jsonPayload = String.format("{\"msgType\":\"eliminated\", \"user\":\"%s\"}", username);
    messagingTemplate.convertAndSend(String.format("/topic/play/%d", gameId), jsonPayload);
  }

  public void useItem(long gameId, String username, String effect) {
    String jsonPayload = String.format(
        "{\"msgType\":\"useItem\", \"username\":\"%s\", \"effect\":\"%s\"}", username, effect);
    messagingTemplate.convertAndSend(String.format("/topic/play/%d", gameId), jsonPayload);
  }

  public void endGameScore(GameResult result) {
    try {
        String JSONresult = mapper.writeValueAsString(result.getResult());
      String jsonPayload = String.format("{\"msgType\":\"end\", "
          + "\"gameId\":\"%d\", "
          + "\"winner1\":\"%s\", "
          + "\"winner2\":\"%s\", "
          + "\"result\":%s}"
          , result.getGameId(), result.getWinner1(), result.getWinner2(), JSONresult);
      messagingTemplate.convertAndSend(String.format("/topic/play/%d", result.getGameId()), jsonPayload);
    } catch (JsonProcessingException e) {
      System.out.println(e.getMessage());
    }
  }

  public void playersCount(long gameId, int playersLeft) {
    String jsonPayload = String.format("{\"msgType\":\"playerCount\", \"count\":\"%d\"}", playersLeft);
    messagingTemplate.convertAndSend(String.format("/topic/play/%d", gameId), jsonPayload);
  }

  public void userList(long gameId, List<String> users){
    EnterMessageResponse message = new EnterMessageResponse("users", users);
    messagingTemplate.convertAndSend(String.format("/topic/waiting/%d", gameId), message);
  }
}
