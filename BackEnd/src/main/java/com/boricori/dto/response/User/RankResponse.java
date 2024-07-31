package com.boricori.dto.response.User;

import lombok.Data;

import java.util.List;

@Data
public class RankResponse {

    private int rank;
    private List<RankDtoResponse> rankAll;

    public RankResponse(int rank, List<RankDtoResponse> rankAll) {
        this.rank = rank;
        this.rankAll = rankAll;
    }
}
