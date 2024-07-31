package com.boricori.controller;

import com.boricori.dto.RoomMessage;
import com.boricori.service.GameRoomService;
import com.boricori.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Controller
public class MessageController {

  private final MessageService messageService;

  @Autowired
  private GameRoomService gameRoomService;

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }


  // Convert LocalDateTime to epoch seconds
  public static long localDateTimeToEpochSeconds(LocalDateTime localDateTime) {
    return localDateTime.toEpochSecond(ZoneOffset.UTC);
  }

  @MessageMapping("/enter")
  public void enterRoom(RoomMessage message) throws Exception {
    String roomId = message.getRoomId();
    String userName = message.getUsername();
    gameRoomService.enterRoom(roomId, userName);

    messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
  }

  @MessageMapping("/leave")
  public void leaveGame(RoomMessage message) {
    String roomId = message.getRoomId();
    String userName = message.getUsername();
    gameRoomService.leaveRoom(roomId, userName);
  }
}