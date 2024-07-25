package com.boricori.dto;

import lombok.Data;

@Data
public class KafkaMessage {

  private String username;

  private String message;
}
