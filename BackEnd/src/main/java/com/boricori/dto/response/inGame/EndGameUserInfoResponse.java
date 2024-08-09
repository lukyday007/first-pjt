package com.boricori.dto.response.inGame;

import com.boricori.entity.GameParticipants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class EndGameUserInfoResponse {
    
    @Schema(description = "생존 여부")
    private boolean alive;
    @Schema(description = "미션 수행 여부")
    private int missionComplete;
    @Schema(description = "킬 수")
    private int kills;
    @Schema(description = "총알 수")
    private int bullets;

    @Builder
    public EndGameUserInfoResponse(boolean alive, int missionComplete, int kills, int bullets) {
        this.alive = alive;
        this.missionComplete = missionComplete;
        this.kills = kills;
        this.bullets = bullets;
    }


    public static EndGameUserInfoResponse of(GameParticipants user){
        return EndGameUserInfoResponse.builder()
                .alive(user.isAlive())
                .missionComplete(user.getMissionComplete())
                .kills(user.getKills())
                .bullets(user.getBullets())
                .build();
    }
}
