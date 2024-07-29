package com.boricori.controller.websocket;

import com.boricori.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GameRoomWebSocket {

    @Autowired
    private GameRoomService gameRoomService;


    @PostMapping("/checkRoom")
    @ResponseBody
    public String checkRoom(@RequestBody String roomId){
        Long id = Long.parseLong(roomId);
        int maxCount = gameRoomService.findMaxPlayerCountRoom(id);
        int curCount = gameRoomService.getCurrentRoomPlayerCount(roomId);
        if(curCount >= maxCount){
            return "false";
        }
        return "true";
    }

}
