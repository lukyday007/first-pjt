package com.boricori.controller;

import com.boricori.service.MessageService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
}