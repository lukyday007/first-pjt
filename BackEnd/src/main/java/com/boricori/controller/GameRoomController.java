package com.boricori.controller;

import com.boricori.dto.request.gameroom.EndGameRoomRequest;
import com.boricori.dto.request.gameroom.GameRequest;
import com.boricori.dto.request.gameroom.GameUpdateRequest;
import com.boricori.dto.request.gameroom.PlayerInfoRequest;
import com.boricori.dto.request.gameroom.StartGameRoomRequest;
import com.boricori.dto.response.gameroom.CreateGameRoomResponse;
import com.boricori.dto.response.gameroom.GameRoomSettingResponse;
import com.boricori.dto.response.gameroom.StartGameRoomResponse;
import com.boricori.dto.response.gameroom.end.EndGameResponse;
import com.boricori.entity.GameRoom;
import com.boricori.service.GameRoomService;
import com.boricori.service.ParticipantsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/gameroom")
@RestController
public class GameRoomController {

  @Autowired
  private GameRoomService gameRoomService;

  @Autowired
  private ParticipantsService participantsService;

  @GetMapping("/create")
  @Operation(summary = "게임방 생성", description = "게임방 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "404", description = "실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<GameRoomSettingResponse> createGameRoom(
      @RequestBody @Parameter(description = "회원가입 정보", required = true) GameRequest gameRequest) {

    return null;
  }

  @GetMapping("/{id}")
  @Operation(summary = "게임방 참여", description = "유저가 선택한 게임방 참여")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "404", description = "실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<CreateGameRoomResponse> enterGameRoom(
      @PathVariable @Parameter(description = "들어갈 방 id", required = true) int id) {

    return null;
  }

  @PatchMapping("/update/setting")
  @Operation(summary = "게임방 설정", description = "기본 값 게임방 설정 변경")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "404", description = "실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<GameRoomSettingResponse> updateGameRoomSetting(
      @RequestBody @Parameter(description = "게임 설정 업데이트", required = true) GameUpdateRequest request) {

    return null;
  }

  @PostMapping("/start")
  @Operation(summary = "게임 시작", description = "게임 시작")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "404", description = "실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<StartGameRoomResponse> startGameRoom(
      @RequestBody @Parameter(description = "게임 시작 서버 데이터 전달", required = true) StartGameRoomRequest request) {
    // 게임 방 튜플 생성
    GameRoom gameRoom = gameRoomService.makeRoom(request);
    // 게임 참여자 튜플 생성 JPA
    participantsService.makeGameParticipant(gameRoom, request.getPlayerInfoRequests());
    // 생성된 게임 방 id를 받음
    // ..
    // 게임 참여 id에 맞게 꼬리잡기 리스트 생성 Map<int, List<ParticipantNameDto>>
    // ..

    List<PlayerInfoRequest> playerInfo = request.getPlayerInfoRequests();

    return null;
  }

  @PatchMapping("/end")
  @Operation(summary = "게임 종료", description = "게임 종료")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "404", description = "실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<EndGameResponse> endGameRoom(
      @RequestBody @Parameter(description = "게임 종료 후 데이터 전달", required = true) EndGameRoomRequest request) {

    return null;
  }
}
