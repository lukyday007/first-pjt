package com.boricori.controller;

import com.boricori.service.GameRoomService;
import com.boricori.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@RequestMapping("/msg")
//@Controller
@RestController
public class MessageController {

  private final MessageService messageService;

  @Autowired
  private GameRoomService gameRoomService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }


  // Convert LocalDateTime to epoch seconds
  public static long localDateTimeToEpochSeconds(LocalDateTime localDateTime) {
    return localDateTime.toEpochSecond(ZoneOffset.UTC);
  }


  @MessageMapping("/start")
  public void startGame(Long gameRoomId){
    messageService.startGame(gameRoomId);
  }

  @MessageMapping("/leave")
  public void leaveGame(Map<String, String> message) {
    String roomId = message.get("roomId");
    gameRoomService.leaveRoom(roomId);
  }
}