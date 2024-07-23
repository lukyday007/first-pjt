package com.boricori.controller;

import com.boricori.dto.GpsSignal;
import com.boricori.service.NotificationService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

  private final SimpMessagingTemplate messagingTemplate;

  private final NotificationService notificationService;


  @Autowired
  public MessageController(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
    this.messagingTemplate = messagingTemplate;
    this.notificationService = notificationService;
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
    // 여기서 꼬리 잡는 상대 가져오기
    Long targetId = 0l;
    messagingTemplate.convertAndSend(String.format("/topic/user/%d", targetId));
  }

  @MessageMapping("/start")
  public void startGame(Long gameRoomId){
    messagingTemplate.convertAndSend(String.format("/topic/room/%d/alert", gameRoomId), "Game Started");
  }


}