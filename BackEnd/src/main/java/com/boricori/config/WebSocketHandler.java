package com.boricori.config;

import com.boricori.service.GameRoomServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private GameRoomServiceImpl gameRoomService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = (String)session.getAttributes().get("username");
        String roomId = session.getUri().getPath().split("/room/")[1];
        String sessionId = session.getId();
        gameRoomService.enterRoom(roomId, sessionId, username);
    }
}
