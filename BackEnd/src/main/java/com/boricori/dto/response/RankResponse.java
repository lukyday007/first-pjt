package com.boricori.dto.response;


import com.boricori.entity.RankData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "유저 랭크")
@Getter
@Setter
public class RankResponse {

    @Schema(description = "순위")
    int rank;
    @Schema(description = "유저 아이디")
    String username;
    @Schema(description = "누적 점수")
    int score;

    public static RankResponse of(RankData rank){
      RankResponse res = new RankResponse();
      res.setRank(rank.getRank());
      res.setUsername(rank.getUsername());
      res.setScore(rank.getScore());
      return res;
    }
}
