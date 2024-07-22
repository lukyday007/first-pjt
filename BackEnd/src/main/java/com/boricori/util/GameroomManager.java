package com.boricori.util;


import com.boricori.entity.GameRoom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

// Redis로 치환 예정
public class GameroomManager {
    private Map<Long, LocalDateTime[]> roomsAndTime = new HashMap<>();
    private Map<Long, GameRoom> rooms = new HashMap<>();

  public void addChatRoom(GameRoom gameRoom) {
    LocalDateTime[] temp = new LocalDateTime[3];
   long duration = Duration.between(gameRoom.getStartTime(), gameRoom.getEndTime()).toSeconds();
   long section = duration % 4;
   for (int i = 0; i < temp.length; i++){
     temp[i] = gameRoom.getStartTime().plusSeconds(section * (i + 1));
   }
    roomsAndTime.put(gameRoom.getId(), temp);
   rooms.put(gameRoom.getId(), gameRoom);
  }

  public LocalDateTime[] getAlertTime(Long roomId) {
    return roomsAndTime.get(roomId);
  }

  public Map<Long, LocalDateTime[]> getGameRooms() {
    return roomsAndTime;
  }

  public LocalDateTime getEndTime(Long id){
    return rooms.get(id).getEndTime();
  }
}
