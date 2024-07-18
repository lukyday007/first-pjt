package com.boricori.dto.response.gameroom.end;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class EndGameResponse {
    
    @Schema(description = "게임 플레이어의 데이터")
    List<EndUserInfoResponse> players;
}
