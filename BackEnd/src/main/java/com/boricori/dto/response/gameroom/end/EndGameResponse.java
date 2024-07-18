package com.boricori.dto.response.gameroom.end;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class EndGameResponse {
    
    @Schema(description = "게임 플레이어의 데이터")
    List<EndUserInfo> players;
}
