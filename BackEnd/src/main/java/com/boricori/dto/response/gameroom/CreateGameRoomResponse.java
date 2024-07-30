package com.boricori.dto.response.gameroom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateGameRoomResponse {
    private Long gameRoomId;
    private String qrCode;
    private String codeNumber;

    public CreateGameRoomResponse(Long id, String qrCode, String codeNumber) {
        this.gameRoomId = id;
        this.qrCode = qrCode;
        this.codeNumber = codeNumber;
    }
}
