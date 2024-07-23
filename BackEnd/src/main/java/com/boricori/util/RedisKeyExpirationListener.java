package com.boricori.util;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener implements MessageListener {

  private final static int GAME_ENDED = 4;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    String expiredKey = new String(message.getBody());
    System.out.println("Key expired: " + expiredKey);

    // 키 형식: gameRoomId-Interval-AlertDegree
    String[] parts = expiredKey.split("-");
    if (parts.length == 3) {
      String gameRoomId = parts[0];
      int interval = Integer.parseInt(parts[1]);
      int alertDegree = Integer.parseInt(parts[2]);

      // alertDegree를 증가시키고 새로운 키 설정
      alertDegree++;
      if (alertDegree < GAME_ENDED) {
        String newKey = gameRoomId + "-" + interval + "-" + alertDegree;
        redisTemplate.opsForValue().set(newKey, "", interval, TimeUnit.SECONDS);
        System.out.println("New key set: " + newKey);
      } else {
        System.out.println("Alert degree exceeded limit for gameRoomId: " + gameRoomId);
      }

      // 필요에 따라 추가적인 처리
    } else {
      System.err.println("Invalid key format: " + expiredKey);
    }
  }
}
