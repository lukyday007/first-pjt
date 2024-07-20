package com.boricori.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Schema(title = "ParticipantsResponse (게임 참여자 상태 정보)", description = "게임에 참여하는 인원의 이름, 생존 여부, 미션 수행 횟수, 잡은 횟수, 총알 갯수를 응답")
public class ParticipantResponse {

  @Schema(description = "유저 이름", example = "홍길동")
  String name;

  @Schema(description = "유저의 생존 여부", example = "true")
  boolean alive;

  @Schema(description = "유저의 미션 수행 횟수", example = "0")
  int missionComplete;

  @Schema(description = "유저가 잡은 횟수", example = "0")
  int kills;

  @Schema(description = "유저의 총알 갯수", example = "0")
  int bullets;
}
