package com.boricori.controller;

import com.boricori.dto.request.inGame.MissionChangeRequest;
import com.boricori.dto.request.inGame.UseItemRequest;
import com.boricori.dto.response.inGame.ItemResponse;
import com.boricori.dto.response.inGame.MissionResponse;
import com.boricori.entity.Item;
import com.boricori.entity.Mission;
import com.boricori.game.GameManager;
import com.boricori.service.InGameService;
import com.boricori.util.ResponseEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
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

  private InGameService inGameService;

  private final GameManager gm = GameManager.getGameManager();

  //임시
  String email = "ssafy";

  @GetMapping("/{roomId}/assignMissions")
  public ResponseEntity<List<MissionResponse>> assignMissions(@PathVariable Long roomId, HttpServletRequest request, HttpServletResponse response){
//    String email = (String) request.getAttribute("email");
    List<Mission> missions = inGameService.assignMissions(email, roomId);
    List<MissionResponse> resp = new ArrayList<>();
    for (Mission m : missions){
      MissionResponse newM = MissionResponse.of(m);
      resp.add(newM);
    }
    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(resp);
  }

  @PostMapping("/{roomId}/changeMission")
  public ResponseEntity<MissionResponse> changeMission(@PathVariable Long gameId, @RequestBody MissionChangeRequest request){
    Mission newMission = inGameService.changeMission(gameId, email, request);
    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(MissionResponse.of(newMission));
  }

  @PostMapping("/{roomId}/completeMission")
  public ResponseEntity<String> completeMission(@PathVariable Long gameId, @RequestBody MissionChangeRequest request){
    inGameService.completeMission(gameId, email, request);
    Item item = inGameService.getItem(gameId, email);
    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body("COMPLETE");
  }

  @GetMapping("/{roomId}/getItem")
  public ResponseEntity<ItemResponse> getItem(@PathVariable Long gameId){
    Item item = inGameService.getItem(gameId, email);
    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(ItemResponse.of(item));
  }

  @PostMapping("/{roomId}/useItem")
  public void useItem(@PathVariable Long gameId, UseItemRequest req){
    inGameService.useItem(gameId, email, req);
  }

  @PostMapping("/{roomId}/catchTarget")
  public void catchTarget(@PathVariable Long gameId){
      //
  }

}
