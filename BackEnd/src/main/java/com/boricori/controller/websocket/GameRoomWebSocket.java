package com.boricori.controller.websocket;

import com.boricori.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class GameRoomWebSocket {

    @Autowired
    private GameRoomService gameRoomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/enter")
    public void enterRoom(RoomMessage message) throws Exception {
        String roomId = message.getRoomId();
        String userName = message.getUsername();
        gameRoomService.enterRoom(roomId, userName);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
    }

    @PostMapping("/gameroom/checkRoom")
    @ResponseBody
    public String checkRoom(@RequestBody String roomId){
        Long id = Long.parseLong(roomId);
        int maxCount = gameRoomService.findMaxPlayerCountRoom(id);
        int curCount = gameRoomService.getCurrentRoomPlayerCount(roomId);
        if(curCount >= maxCount){
            return "full";
        }
        return "true";
    }

}
