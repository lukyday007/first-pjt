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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private GameRoomServiceImpl gameRoomService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, List<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = (String)session.getAttributes().get("username");
        String roomId = session.getUri().getPath().split("/gameRoom/")[1];

        List<String> players = gameRoomService.enterRoom(roomId, username);
        roomSessions.compute(roomId, (key, sessions) -> {
            if (sessions == null) {
                sessions = new ArrayList<>();
            }
            synchronized (sessions) {
                sessions.add(session);
            }
            return sessions;
        });
        ChangeListJsonAndSend(session, roomId, players);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String)session.getAttributes().get("username");
        String roomId = session.getUri().getPath().split("/gameRoom/")[1];
        System.out.println("close: "+username+" // "+ roomId);
        List<String> players = gameRoomService.leaveRoom(roomId, username);
        roomSessions.get(roomId).remove(session);
        ChangeListJsonAndSend(session, roomId, players);
    }

    private void ChangeListJsonAndSend(WebSocketSession originSession, String roomId, List<String> players) throws IOException {
        List<WebSocketSession> sessions = roomSessions.get(roomId);
        String jsonMessage = objectMapper.writeValueAsString(players);

        if (sessions != null) {
            synchronized (sessions) {
                for (WebSocketSession session : sessions) {
                    if (session.isOpen() && !session.getId().equals(originSession.getId())) {
                        session.sendMessage(new TextMessage(jsonMessage));
                    }
                }
            }
        }
    }
}
