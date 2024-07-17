package com.boricori.controller;


import com.boricori.dto.response.UserResponse;
import com.boricori.entity.Rank;
import com.boricori.entity.User;
import com.boricori.service.UserService;
import com.boricori.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import jdk.javadoc.doclet.Reporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping("/login")
  public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest loginRequest){
    int res = userService.login(loginRequest);
    if (res == 1){
      String token = jwtUtil.tokenize(loginRequest.getEmail);
      return new ResponseEntity<UserLoginResponse>(200, UserLoginResponse.of(token));
    }
    return new ResponseEntity<UserLoginResponse>(400, null);
  }


  @PostMapping("/signup")
  public ResponseEntity<UserSignUpResponse> signup(@RequestBody UserSignUpRequest signUpRequest){
    User user = userService.signup(signUpRequest);
    if (user != null){
      return new ResponseEntity<UserSignUpResponse>(200, UserSignUpResponse.of(user));
    }
    return new ResponseEntity<UserSignUpResponse>(400, null);
  }

  // 로그아웃은 프론트에서 처리, 백에서 할 일 없음
  @GetMapping("/logout")
  public BaseResponse<?> logout(){
    return null;
  }

  @GetMapping("/ranks")
  public ResponseEntity<RankResponse> getRanks(){
    List<Rank> res = userService.getRanks();
    RankResponse ranks = res.stream().map(rank -> RankResponse.of(rank)).collect().toList();
    if (res.size() == 0){
      return new ResponseEntity<>(406);
    }
    return new ResponseEntity<RankResponse>(200, ranks);
  }

  @GetMapping("/myProfile")
  public UserResponse myProfile(HttpServletRequest req){
    String username = req.getUsername();
    User user = userService.getUser(username);
    return new UserResponse.of(200, user);
  }

  @PatchMapping("updateProfile")
  public ResponseEntity<UserResponse> updateProfile(HttpServletRequest req, @RequestBody UserUpdateRequest updateRequest) {
    String username = req.getUsername();
    User user = userService.updateUser(updateRequest, username);
    return new ResponseEntity<>(200, UserResponse.of(user));
  }

  @GetMapping("/profile/{id}")
  public ResponseEntity<UserResponse> getProfile(@PathVariable String id){
    User user = userService.getUser(id);
    return new ResponseEntity<>(200, UserResponse.of(user));
  }
}
