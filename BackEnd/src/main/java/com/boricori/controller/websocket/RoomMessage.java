package com.boricori.controller.websocket;

import lombok.Data;

@Data
public class RoomMessage {
    private String roomId;
    private String username;
}
