package com.boricori.controller;

import com.boricori.dto.request.inGame.InGameRequest;
import com.boricori.dto.request.inGame.MissionChangeRequest;
import com.boricori.dto.request.inGame.UseItemRequest;
import com.boricori.dto.response.inGame.ItemResponse;
import com.boricori.dto.response.inGame.MissionResponse;
import com.boricori.entity.Item;
import com.boricori.entity.Mission;
import com.boricori.entity.User;
import com.boricori.game.GameManager;
import com.boricori.service.GameService;
import com.boricori.service.InGameService;
import com.boricori.service.MessageService;
import com.boricori.service.UserService;
import com.boricori.util.Node;
import com.boricori.util.ResponseEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "게임 컨트롤러", description = "게임 진행 중 일어나는 이벤트 관리")
@RestController
@RequestMapping("/in-game")
public class InGameController {

  @Autowired
  private InGameService inGameService;

  private final GameManager gameManager = GameManager.getGameManager();

  @Autowired
  private UserService userService;

  @Autowired
  private MessageService messageService;

  @PostMapping("/assignMissions")
  public ResponseEntity<List<MissionResponse>> assignMissions(@RequestBody InGameRequest request){
    String username = request.getUsername();
    long gameId = request.getGameId();
    List<Mission> missions = inGameService.assignMissions(username, gameId);
    List<MissionResponse> resp = new ArrayList<>();
    for (Mission m : missions){
      MissionResponse newM = MissionResponse.of(m);
      resp.add(newM);
    }
    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(resp);
  }

  @Transactional
  @PostMapping("/changeMission")
  public ResponseEntity<MissionResponse> changeMission(@RequestBody MissionChangeRequest request){
    String username = request.getUsername();
    long gameId = request.getGameId();
    long missionId = request.getMissionId();
    Mission newMission = inGameService.changeMission(gameId, username, missionId);
    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(MissionResponse.of(newMission));
  }

  @Transactional
  @PostMapping("/completeMission")
  public ResponseEntity<ItemResponse> completeMission(@RequestBody MissionChangeRequest request){
    String username = request.getUsername();
    long gameId = request.getGameId();
    long missionId = request.getMissionId();
    inGameService.completeMission(gameId, username, missionId);
    Item item = inGameService.getItem(gameId, username);
    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(ItemResponse.of(item));
  }

//  @GetMapping("/{gameId}/getItem")
//  public ResponseEntity<ItemResponse> getItem(@PathVariable Long gameId){
//    Item item = inGameService.getItem(gameId, email);
//    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(ItemResponse.of(item));
//  }

  @Transactional
  @PostMapping("/useItem")
  public void useItem(@RequestBody UseItemRequest request){
    String username = request.getUsername();
    long gameId = request.getGameId();
    long itemId = request.getItemId();
    inGameService.useItem(gameId, username, itemId);
  }

  @Transactional
  @PostMapping("/catchTarget")
  public void catchTarget(@RequestBody InGameRequest request){
    String username = request.getUsername();
    long gameId = request.getGameId();
    Node<User> targetNode = GameManager.catchableList.get(gameId).killTarget(username);
    Node<User> newTarget = targetNode.next;
    messageService.changeTarget(username, newTarget.data.getUsername(), gameId);
    messageService.notifyStatus(targetNode.data.getUsername(), gameId);
    User user = userService.findByUsername(username);
    inGameService.catchTarget(user, targetNode.data, gameId);
  }

}
