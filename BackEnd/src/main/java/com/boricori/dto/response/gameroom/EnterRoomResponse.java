package com.boricori.dto.response.gameroom;

import lombok.Data;

@Data
public class EnterRoomResponse {
    private String result;
    private Long gameId;

    public EnterRoomResponse(String result, Long gameId){
      this.result = result;
      this.gameId = gameId;
    }

}
