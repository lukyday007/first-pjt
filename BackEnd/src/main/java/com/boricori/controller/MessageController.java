package com.boricori.controller;

import com.boricori.dto.GpsSignal;
import com.boricori.util.GameroomManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class MessageController {

  private final SimpMessagingTemplate messagingTemplate;

  private final GameroomManager gameroomManager;

  @Autowired
  public MessageController(SimpMessagingTemplate messagingTemplate, GameroomManager manager) {
    this.messagingTemplate = messagingTemplate;
    this.gameroomManager = manager;
  }

//  @MessageMapping("/hello")
//  @SendTo("/topic/greetings")
//  public Greeting greeting(HelloMessage message) throws Exception {
//    Thread.sleep(1000); // simulated delay
//    return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//  }


  @MessageMapping("/locate")
  public void location(GpsSignal gps) throws Exception {
    String host = gps.getHost();
    // 여기서 꼬리 잡는 상대 가져오기
    Long targetId = 0l;
    messagingTemplate.convertAndSend(String.format("/topic/%d", targetId));
  }

  @MessageMapping("/start")
  public void startGame(Long gameRoomId){
    messagingTemplate.convertAndSend(String.format("/topic/room/%d/general", gameRoomId));
  }


  @Scheduled(fixedRate = 1000) // 1초마다 실행
  public void checkTimeAndSendMessages() {
    LocalTime now = LocalTime.now();
    for (Map.Entry<Long, LocalDateTime[]> entry : gameroomManager.getGameRooms().entrySet()) {
      Long id = entry.getKey();
      LocalDateTime[] times = entry.getValue();
      for (LocalDateTime t : times){
        if (now.getHour() == t.getHour() && now.getMinute() == t.getMinute() && now.getSecond() == t.getSecond()) {
          messagingTemplate.convertAndSend(String.format("/topic/room/%d/general", id) , "Alert for room " + id + " at " + entry.getValue());
        }
      }
      LocalDateTime end = gameroomManager.getEndTime(id);
      if (now.getHour() == end.getHour() && now.getMinute() == end.getMinute() && now.getSecond() == end.getSecond()){
        messagingTemplate.convertAndSend(String.format("/topic/room/%d/general", id) , "게임이 종료되었습니다");
      }

    }
  }

}