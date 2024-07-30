package com.boricori.controller;

import com.boricori.dto.request.gameroom.EndGameRoomRequest;
import com.boricori.dto.request.gameroom.GameRequest;
import com.boricori.dto.request.gameroom.StartGameRoomRequest;
import com.boricori.dto.response.gameroom.CreateGameRoomResponse;
import com.boricori.dto.response.gameroom.end.EndGameResponse;
import com.boricori.entity.GameRoom;
import com.boricori.entity.User;
import com.boricori.game.GameManager;
import com.boricori.service.GameRoomService;
import com.boricori.service.MessageService;
import com.boricori.service.ParticipantsService;
import com.boricori.util.UserCircularLinkedList;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.boricori.util.ResponseEnum.SUCCESS;

@RequestMapping("/gameroom")
@RestController
public class GameRoomController {

  @Autowired
  private GameRoomService gameRoomService;

  @Autowired
  private ParticipantsService participantsService;


  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private MessageService messageService;


  @PostMapping("/create")
  @Operation(summary = "게임방 생성", description = "게임방 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "404", description = "실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<CreateGameRoomResponse> createGameRoom(
      @RequestBody @Parameter(description = "게임방 세팅", required = true) GameRequest gameRequest) {
    try {
      CreateGameRoomResponse room = gameRoomService.createRoom(gameRequest);
      return ResponseEntity.status(200).body(room);
    } catch (WriterException | IOException e) {
      return ResponseEntity.status(500).build();
    }
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

  @PostMapping("/{id}/start")
  @Operation(summary = "게임 시작", description = "게임 시작")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "404", description = "실패"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  public ResponseEntity<String> startGameRoom(
          @PathVariable @Parameter(description = "게임 방id", required = true) long id,
      @RequestBody @Parameter(description = "게임 시작 서버 데이터 전달", required = true) StartGameRoomRequest request) {
    // 게임 방 튜플 생성
    GameRoom gameRoom = gameRoomService.updateRoom(id, request);
    // 게임 참여자 튜플 생성 JPA
    List<User> users = participantsService.makeGameParticipant(gameRoom,
        request.getPlayerInfoRequests());
    // 게임 참여 방 id에 맞게 꼬리잡기 리스트 생성 Map<int, List<ParticipantNameDto>>
    makeCatchableList(gameRoom.getId(), users);

    // 알림 시간 Redis에 넣기
    int interval = gameRoom.getGameTime() * 60 / 4;
    for (int t = 1; t < 5; t++){
      redisTemplate.opsForValue().set(String.format("%d-%d", gameRoom.getId(), t), String.valueOf(t), interval * t, TimeUnit.SECONDS);
    }
    messageService.startGame(gameRoom.getId(), gameRoom);

    // TODO: Response MongoDB 추가 여부에 따라 Response 달라짐
    return ResponseEntity.status(SUCCESS.getCode()).body("GAME STARTED");
  }

  private void makeCatchableList(Long roomId, List<User> users) {
    GameManager gameManager = GameManager.getGameManager();
    gameManager.shuffleUsers(users);
    UserCircularLinkedList userCircularLinkedList = gameManager.makeUserCatchableList(users);

    GameManager.catchableList.put(roomId, userCircularLinkedList);
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
