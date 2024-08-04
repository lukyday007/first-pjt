package com.boricori.service;

import com.boricori.dto.request.gameroom.GameRequest;
import com.boricori.dto.request.gameroom.StartGameRoomRequest;
import com.boricori.dto.response.gameroom.CreateGameRoomResponse;
import com.boricori.entity.GameRoom;
import com.boricori.repository.GameRoomRepo.GameRoomRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.transaction.Transactional;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GameRoomServiceImpl implements GameRoomService {

  private static final Logger log = LoggerFactory.getLogger(GameRoomServiceImpl.class);
  @Autowired
  private GameRoomRepository gameRoomRepository;
  @Autowired
  private RedisTemplate<String, Object> redisObjectTemplate;
  @Autowired
  private RedissonClient redissonClient;

  @Override
  public GameRoom findGame(Long id){
    return gameRoomRepository.findById(id).orElse(null);
  }

  @Override
  @Transactional
  public CreateGameRoomResponse createRoom(GameRequest gameRoomInfo, String userName) throws IOException, WriterException {
    GameRoom gameRoom = GameRoom.builder().gameRoomRequest(gameRoomInfo).build();
    String currTime = String.valueOf(System.currentTimeMillis());
    String code = currTime.substring(currTime.length() - 8, currTime.length());
    gameRoom.setCodeNumber(code);
    gameRoom = gameRoomRepository.save(gameRoom);
    String roomUrl = "http://runtail/join-room/" + gameRoom.getId();
    String qrCode = generateQRCodeImage(roomUrl);
    gameRoom.createQrCode(qrCode);
    CreateGameRoomResponse response = new CreateGameRoomResponse(gameRoom.getId(), qrCode,
        gameRoom.getGameCode());

    return response;
  }

  @Override
  @Transactional
  public GameRoom updateRoom(Long id, StartGameRoomRequest request) {
    GameRoom gameRoom = gameRoomRepository.findById(id).orElseThrow(IllegalAccessError::new);
    gameRoom.startGameTime();
    gameRoom.setCenter(request.getCenterLat(), request.getCenterLng());
    return gameRoom;
  }

  @Override
  public int findMaxPlayerCountRoom(Long id){
    return gameRoomRepository.findMaxPlayerByRoomId(id);
  }

  @Override
  public int getCurrentRoomPlayerCount(String roomId) {
    RLock lock = redissonClient.getLock("lock:room:" + roomId);
    boolean acquired = false;
    try {
      // 락을 획득 (최대 10초 동안 대기, 10초 동안 유지)
      acquired = lock.tryLock(10, 10, TimeUnit.SECONDS);
      if (acquired) {
        Map<String, String> room = (Map<String, String>) redisObjectTemplate.opsForHash().get("roomId", roomId);
        return room != null ? room.size() : 0;
      } else {
        log.info("Unable to acquire lock for room: {}" , roomId);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      if (acquired) {
        lock.unlock();
      }
    }
    return 0;
  }

  @Override
  public void enterRoom(String roomId, String sessionId, String userName) {
    RLock lock = redissonClient.getLock("lock:room:" + roomId);
    boolean acquired = false;
    try {
      acquired = lock.tryLock(10, 10, TimeUnit.SECONDS);
      if (acquired) {
        Map<String, Object> room = (Map<String, Object>) redisObjectTemplate.opsForHash().get("roomId", roomId);
        if (room == null) {
          room = new HashMap<>();
        }
        room.put(sessionId, userName);
        redisObjectTemplate.opsForHash().put("roomId", roomId, room);
      } else {
        log.info("{} 아직 들어갈 수 없습니다", roomId);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      if (acquired) {
        lock.unlock();
      }
    }
  }

  @Override
  public List<String> leaveRoom(String roomId, String sessionId) {
    RLock lock = redissonClient.getLock("lock:room:" + roomId);
    boolean acquired = false;
    List<String> result = Collections.emptyList();
    try {
      acquired = lock.tryLock(10, 10, TimeUnit.SECONDS);
      if (acquired) {
        Map<Object, Object> room = redisObjectTemplate.opsForHash().entries(roomId);
        if (room != null && room.containsKey(sessionId)) {
          redisObjectTemplate.opsForHash().delete(roomId, sessionId);
          // room 데이터를 다시 가져옴
          room = redisObjectTemplate.opsForHash().entries(roomId);
          result = room.values().stream()
                  .map(Object::toString)
                  .collect(Collectors.toList());
        } else {
          log.info("유저가 떠난 방을 찾을 수 없습니다.");
        }
      } else {
        log.info("떠나야 하는 {}방을 아직 들어갈 수 없습니다", roomId);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      if (acquired && lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
    return result;
  }

  @Override
  public GameRoom findGameByCode(String gameCode) {
    return gameRoomRepository.findByGameCode(gameCode);
  }

  private String generateQRCodeImage(String text) throws IOException, WriterException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);

    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
    byte[] pngData = pngOutputStream.toByteArray();
    return Base64.getEncoder().encodeToString(pngData);
  }

  @Override
  public List<String> GameRoomPlayerAll(String roomId) {
    RLock lock = redissonClient.getLock("lock:room:" + roomId);
    boolean acquired = false;
    List<String> result = Collections.emptyList();
    try {
      // 락을 획득 (최대 10초 동안 대기, 10초 동안 유지)
      acquired = lock.tryLock(10, 10, TimeUnit.SECONDS);
      if (acquired) {
        Map<String, String> room = (Map<String, String>) redisObjectTemplate.opsForHash().get("roomId", roomId);
        if (room != null) {
          result = room.values().stream()
                  .map(Object::toString)
                  .collect(Collectors.toList());
        } else {
          log.info("getUsers를 위한 {}방에 아직 들어갈 수 없습니다", roomId);
        }
      } else {
        log.info("getUsers를 위한 방{}에 들어갈 수 없습니다", roomId);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      if (acquired && lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
    return result;
  }
}
