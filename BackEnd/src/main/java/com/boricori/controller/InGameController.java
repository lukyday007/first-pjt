package com.boricori.controller;

import com.boricori.dto.request.inGame.MissionChangeRequest;
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
  String email = "";

  @GetMapping("/{roomId}/assignMissions")
  public ResponseEntity<List<MissionResponse>> assignMissions(@PathVariable Long roomId, HttpServletRequest request, HttpServletResponse response){
//    String email = (String) request.getAttribute("email");
    List<Mission> missions = inGameService.assignMissions(email, roomId);
    List<MissionResponse> resp = new ArrayList<>();
    for (Mission m : missions){
      MissionResponse newM = MissionResponse.builder()
          .missionId(m.getId())
          .category(m.getCategory())
          .target(m.getTarget())
          .targetEn(m.getTargetEn())
          .build();
      resp.add(newM);
    }
    return ResponseEntity.status(ResponseEnum.SUCCESS.getCode()).body(resp);
  }

  @PostMapping("/{roomId}/changeMission")
  public void changeMission(@PathVariable Long gameId, @RequestBody MissionChangeRequest request){
    inGameService.changeMission(gameId, email, request);
  }

  @PostMapping("/{roomId}/completeMission")
  public void completeMission(@PathVariable Long gameId, @RequestBody MissionChangeRequest request){
    inGameService.completeMission(gameId, email, request);
    Item item = inGameService.getItem(gameId, email);
  }

  @PostMapping("/{roomId}/getItem")
  public void getItem(@PathVariable Long gameId){
    inGameService.getItem(gameId, email);
  }

  @PostMapping("/{roomId}/useItem")
  public void useItem(@PathVariable Long gameId){
    inGameService.useItem(gameId, email);
  }

  @PostMapping("/{roomId}/catchTarget")
  public void catchTarget(@PathVariable Long gameId){
      //
  }

}
