package com.boricori.dto.response.gameroom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class MissionResponse {

    @Schema(description = "미션 이름")
    private String mission;

    @Schema(description = "카테고리")
    private int category;
}
