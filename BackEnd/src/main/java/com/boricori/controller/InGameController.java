package com.boricori.controller;

import com.boricori.dto.request.inGame.MissionChangeRequest;
import com.boricori.dto.request.inGame.UseItemRequest;
import com.boricori.dto.response.inGame.ItemResponse;
import com.boricori.dto.response.inGame.MissionResponse;
import com.boricori.entity.GameRoom;
import com.boricori.entity.Item;
import com.boricori.entity.Mission;
import com.boricori.entity.User;
import com.boricori.game.GameManager;
import com.boricori.service.InGameService;
import com.boricori.util.ResponseEnum;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  private final GameManager gm = GameManager.getGameManager();

  @GetMapping("/{gameId}/assignMissions")
  public ResponseEntity<List<MissionResponse>> assignMissions(@PathVariable Long gameId, HttpServletRequest req, HttpServletResponse response){
    String username = (String) req.getAttribute("username");
    List<Mission> missions = inGameService.assignMissions(username, gameId);
    List<MissionResponse> resp = new ArrayList<>();
    for (Mission m : missions){
      MissionResponse newM = MissionResponse.of(m);
      resp.add(newM);
    }
    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(resp);
  }

  @Transactional
  @PostMapping("/{gameId}/changeMission")
  public ResponseEntity<MissionResponse> changeMission(@PathVariable Long gameId, @RequestBody MissionChangeRequest request,
      HttpServletRequest req){
    String username = (String) req.getAttribute("username");
    Mission newMission = inGameService.changeMission(gameId, username, request);
    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(MissionResponse.of(newMission));
  }

  @Transactional
  @PostMapping("/{gameId}/completeMission")
  public ResponseEntity<ItemResponse> completeMission(@PathVariable Long gameId, @RequestBody MissionChangeRequest request,
      HttpServletRequest req){
    String username = (String) req.getAttribute("username");
    inGameService.completeMission(gameId, username, request);
    Item item = inGameService.getItem(gameId, username);
    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(ItemResponse.of(item));
  }

//  @GetMapping("/{gameId}/getItem")
//  public ResponseEntity<ItemResponse> getItem(@PathVariable Long gameId){
//    Item item = inGameService.getItem(gameId, email);
//    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(ItemResponse.of(item));
//  }

  @Transactional
  @PostMapping("/{gameId}/useItem")
  public void useItem(@PathVariable Long gameId, UseItemRequest request, HttpServletRequest req){
    String username = (String) req.getAttribute("username");
    inGameService.useItem(gameId, username, request);
  }

  @PostMapping("/{roomId}/catchTarget")
  public void catchTarget(@PathVariable Long gameId){
      //
  }

}
