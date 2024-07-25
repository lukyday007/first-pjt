package com.boricori.controller;

import com.boricori.dto.GpsSignal;
import com.boricori.service.MessageService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.KafkaUtils;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }


  // Convert LocalDateTime to epoch seconds
  public static long localDateTimeToEpochSeconds(LocalDateTime localDateTime) {
    return localDateTime.toEpochSecond(ZoneOffset.UTC);
  }


  // 클라이언트단에서 유저의 현재 GPS 정보를 웹소켓 통신으로 send('app/location')한 상태
  // 사용하는건 STOMP Message Broker
  // 받은 정보를 kafka의 topic: game-location으로 보낸다
  // header는 직렬화
  // value는 JSON화
  @MessageMapping("/location")
  public void sendGPS(GpsSignal gps) throws Exception {
    messageService.sendGPS(gps);

  }



  // kafka의 "game-location" topic에 들어간 데이터 수신
  @KafkaListener(topics = "game-location", groupId = "ssafy")
  public void receiveGPSInfo(ConsumerRecord<String, String> record,
      @Header("gameId") byte[] gameIdBytes,
      @Header("sender") byte[] senderBytes){
    String gameId = new String(gameIdBytes, StandardCharsets.UTF_8);
    String sender = new String(senderBytes, StandardCharsets.UTF_8);
    String gpsJSON = record.value();
    messageService.receiveGPSInfo(gameId, sender, gpsJSON);
  }


  @MessageMapping("/start")
  public void startGame(Long gameRoomId){
    messageService.startGame(gameRoomId);
  }


  // Redis에서 kafka로 들어간 게임 시간 주기당 알림 수신
  // alertJSON의 포맷은
  // {"system-message": String,  ("START", "END")
  //  "alert-degree": int }
  @KafkaListener(topics = "game-alert", groupId = "ssafy")
  public void processAlertMessage(ConsumerRecord<String, String> record,
      @Header(KafkaHeaders.RECEIVED_KEY) byte[] gameIdBytes) {
    String grpId = KafkaUtils.getConsumerGroupId();
    System.out.println("GROUP" + grpId);
    System.out.println("GAME-ALERT CONSUMER");
    String gameId = new String(gameIdBytes, StandardCharsets.UTF_8);
    String alertJSON = record.value();
    System.out.println("MESSAGE:" + alertJSON);
    messageService.processAlertMessage(gameId, alertJSON);
  }


}