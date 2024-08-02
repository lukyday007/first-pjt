package com.boricori.config;

import com.boricori.service.GameRoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private GameRoomServiceImpl gameRoomService;

    Map<String, String> userNameSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        String username = (String)session.getAttributes().get("username");
        String roomId = session.getUri().getPath().split("/gameRoom/")[1];
        userNameSessions.put(sessionId, username);
        gameRoomService.enterRoom(roomId, username);

        System.out.println("websocket connected roomId : "+ roomId +" username : "+username);
    }
}
