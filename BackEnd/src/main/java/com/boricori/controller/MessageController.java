package com.boricori.controller;

import com.boricori.controller.websocket.RoomMessage;
import com.boricori.service.GameRoomService;
import com.boricori.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Controller
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

  @MessageMapping("/leave")
  public void leaveGame(RoomMessage message) {
    String roomId = message.getRoomId();
    String userName = message.getUsername();
    gameRoomService.leaveRoom(roomId, userName);
  }
}