package com.boricori.controller;

import com.boricori.dto.GpsSignal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

  private final SimpMessagingTemplate messagingTemplate;

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public MessageController(SimpMessagingTemplate messagingTemplate, KafkaTemplate<String, String> kafkaTemplate) {
    this.messagingTemplate = messagingTemplate;
    this.kafkaTemplate = kafkaTemplate;
  }

  // Convert LocalDateTime to epoch seconds
  public static long localDateTimeToEpochSeconds(LocalDateTime localDateTime) {
    return localDateTime.toEpochSecond(ZoneOffset.UTC);
  }

//  @MessageMapping("/hello")
//  @SendTo("/topic/greetings")
//  public Greeting greeting(HelloMessage message) throws Exception {
//    Thread.sleep(1000); // simulated delay
//    return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//  }


  @MessageMapping("/location")
  public void location(GpsSignal gps) throws Exception {
    String host = gps.getHost();
    messagingTemplate.convertAndSend(String.format("/topic/user/%s", host));
  }

  // pub/start 하면 여기로 옴
  @MessageMapping("/start")
  public void startGame(Long gameRoomId){
    messagingTemplate.convertAndSend(String.format("/topic/room/%d/alert", gameRoomId), "Game Started");
  }


}