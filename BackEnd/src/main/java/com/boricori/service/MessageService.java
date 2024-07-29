package com.boricori.service;

import com.boricori.dto.GpsSignal;
import com.boricori.game.GameManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  private final SimpMessagingTemplate messagingTemplate;

  private final GameManager gameManager = GameManager.getGameManager();

  private final ObjectMapper mapper = new ObjectMapper();


  public MessageService(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }


  public void startGame(Long gameRoomId){
    // 여기는 현재 방에 웹소켓으로 연결된 유저들에게 알림만 주면 되므로 kafka 거칠 필요 없음
    // pub('app/start') 받아서 sub('/topic/general/roomId') 한 유저들에게 뿌려줌
    String startJSON = "{'msgType':'start'}";
    messagingTemplate.convertAndSend(String.format("/topic/alert/%d", gameRoomId), startJSON);
  }

  public void processAlertMessage(String gameId, String alertJSON) {
    messagingTemplate.convertAndSend(String.format("/topic/alert/%s", gameId), alertJSON);
  }

}
