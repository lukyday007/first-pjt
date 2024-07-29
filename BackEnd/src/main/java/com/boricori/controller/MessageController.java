package com.boricori.controller;

import com.boricori.service.MessageService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/msg")
//@Controller
@RestController
public class MessageController {

  private final MessageService messageService;

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


}