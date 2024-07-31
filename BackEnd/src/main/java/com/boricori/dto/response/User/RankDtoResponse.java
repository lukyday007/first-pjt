package com.boricori.dto.response.User;


import com.boricori.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "유저 랭크")
@Getter
@Setter
public class RankDtoResponse {

    @Schema(description = "순위")
    int rank;
    @Schema(description = "유저 아이디")
    String username;
    @Schema(description = "누적 점수")
    int score;

    public RankDtoResponse(int rank, User user) {
        this.rank = rank;
        this.username = user.getUsername();
        this.score = user.getScores();
    }
}
