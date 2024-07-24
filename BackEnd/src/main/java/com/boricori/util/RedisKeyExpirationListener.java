package com.boricori.util;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener implements MessageListener {

  private final static int GAME_ENDED = 4;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String expiredKey = new String(message.getBody());
    System.out.println("Key expired: " + expiredKey);

    // 키 형식: gameRoomId-AlertDegree
    String[] parts = expiredKey.split("-");
    if (parts.length == 2) {
      String gameRoomId = parts[0];
      int alertDegree = Integer.parseInt(parts[1]);
      // kafka의 topic: game-alert에 보내놓기
      String jsonData = String.format("{'system-message':null, 'alert-degree':%d}", alertDegree);
      kafkaTemplate.send("game-alert", jsonData);
    } else {
      System.err.println("Invalid key format: " + expiredKey);
    }
  }
}
