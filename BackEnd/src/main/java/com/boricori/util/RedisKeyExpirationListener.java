package com.boricori.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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
      String jsonData = String.format("{'msgType':alert, 'alert-degree':%d}", alertDegree);
      CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("game-alert", gameRoomId, jsonData);
      future.thenAccept(result -> {
        System.out.println("Completed successfully with result: " + result);
      });
      future.exceptionally(ex -> {
        System.err.println("Failed with exception: " + ex.getMessage());
        return null;  // 예외 발생 시 기본 값을 반환할 수 있습니다.
      });
    } else {
      System.err.println("Invalid key format: " + expiredKey);
    }
  }
}
