package com.boricori.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class  NotificationService {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  private static final String GLOBAL_NOTIFICATIONS_KEY = "global_notifications";

  private String formatTimestamp(long timestamp) {
    return DateTimeFormatter.ISO_INSTANT
        .withZone(ZoneId.systemDefault())
        .format(Instant.ofEpochSecond(timestamp));
  }

  public void addNotification(String gameId, long timestamp, String degree) {
//    String formattedTimestamp = formatTimestamp(timestamp);
    String value = gameId + ":" + degree;
    redisTemplate.opsForZSet().add(GLOBAL_NOTIFICATIONS_KEY, value, timestamp);
  }


  public Set<String> getNotificationsBefore(long timestamp) {
    // 1. 조회: 특정 시간 이전의 알림을 조회합니다.
    Set<String> notificationsBefore = redisTemplate.opsForZSet().rangeByScore(GLOBAL_NOTIFICATIONS_KEY, Double.NEGATIVE_INFINITY, timestamp);

    if (notificationsBefore != null && !notificationsBefore.isEmpty()) {
      // 2. 삭제: 조회된 알림을 Redis에서 삭제합니다.
      redisTemplate.opsForZSet().removeRangeByScore(GLOBAL_NOTIFICATIONS_KEY, Double.NEGATIVE_INFINITY, timestamp);
    }

    return notificationsBefore;
  }

  public Set<String> getNotificationsAfter(long timestamp) {
    return redisTemplate.opsForZSet().rangeByScore(GLOBAL_NOTIFICATIONS_KEY, timestamp, Double.POSITIVE_INFINITY);
  }
}
