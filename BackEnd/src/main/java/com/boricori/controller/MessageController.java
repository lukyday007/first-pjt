package com.boricori.controller;

import com.boricori.dto.GpsSignal;
import com.boricori.game.GameManager;
import com.boricori.util.UserCircularLinkedList;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

  private final SimpMessagingTemplate messagingTemplate;

  private final KafkaTemplate<String, String> kafkaTemplate;

  private final GameManager gameManager = GameManager.getGameManager();

  private final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  public MessageController(SimpMessagingTemplate messagingTemplate, KafkaTemplate<String, String> kafkaTemplate) {
    this.messagingTemplate = messagingTemplate;
    this.kafkaTemplate = kafkaTemplate;
  }

  // Convert LocalDateTime to epoch seconds
  public static long localDateTimeToEpochSeconds(LocalDateTime localDateTime) {
    return localDateTime.toEpochSecond(ZoneOffset.UTC);
  }

  // 참고용
//  @MessageMapping("/hello")
//  @SendTo("/topic/greetings")
//  public Greeting greeting(HelloMessage message) throws Exception {
//    Thread.sleep(1000); // simulated delay
//    return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//  }

  // 클라이언트단에서 유저의 현재 GPS 정보를 웹소켓 통신으로 send('app/location')한 상태
  // 사용하는건 STOMP Message Broker
  // 받은 정보를 kafka의 topic: game-location으로 보낸다
  // header는 직렬화
  // value는 JSON화
  @MessageMapping("/location")
  public void sendGPS(GpsSignal gps) throws Exception {
    String json = mapper.writeValueAsString(gps);
    var record = new ProducerRecord<String, String>("game-location", json);
    record.headers().add("gameId", String.valueOf(gps.getGameId()).getBytes(StandardCharsets.UTF_8))
        .add("sender", gps.getHost().getBytes(StandardCharsets.UTF_8));
    kafkaTemplate.send(record);
  }


  // kafka의 "game-location" topic에 들어간 데이터 수신
  @KafkaListener(topics = "game-location", groupId = "ssafy")
  public void receiveGPSInfo(ConsumerRecord<String, String> record,
      @Header("gameId") byte[] gameIdBytes,
      @Header("sender") byte[] senderBytes){
      String gameId = new String(gameIdBytes, StandardCharsets.UTF_8);
      String sender = new String(senderBytes, StandardCharsets.UTF_8);
      String gpsJSON = record.value();
      messagingTemplate.convertAndSend(String.format("/topic/location/%s", sender), gpsJSON);

  }


  // 방장이 게임 시작버튼 누르고 자동적으로 STOMP MB에 send('app/start', 'start') 할 경우
  // Redis의 expire event 알림 메시지는 kafka로 바로 pub 하므로 이 messageMapping 거치지 않음
  @MessageMapping("/start")
  public void startGame(Long gameRoomId){
    // 여기는 현재 방에 웹소켓으로 연결된 유저들에게 알림만 주면 되므로 kafka 거칠 필요 없음
    // pub('app/start') 받아서 sub('/topic/general/roomId') 한 유저들에게 뿌려줌
    messagingTemplate.convertAndSend(String.format("/topic/alert/%d", gameRoomId));
  }


  // Redis에서 kafka로 들어간 게임 시간 주기당 알림 수신
  // alertJSON의 포맷은
  // {"system-message": String,  ("START", "END")
  //  "alert-degree": int }
  @KafkaListener(topicPattern = "game-alert", groupId = "ssafy")
  public void processAlertMessage(ConsumerRecord<String, String> record,
      @Header(KafkaHeaders.KEY) byte[] gameIdBytes) {
      String gameId = new String(gameIdBytes, StandardCharsets.UTF_8);
      String alertJSON = record.value();
      messagingTemplate.convertAndSend(String.format("/topic/alert/%s", gameId), alertJSON);
  }


}