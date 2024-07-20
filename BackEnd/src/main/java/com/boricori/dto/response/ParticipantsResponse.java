package com.boricori.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
@Schema(title = "Participants (게임에 참염자들 정보)", description = "게임에 참여하는 모든 인원의")
public class ParticipantsResponse {

  @Schema(description = "게임 참여자 정보")
  List<ParticipantResponse> participants;

}
