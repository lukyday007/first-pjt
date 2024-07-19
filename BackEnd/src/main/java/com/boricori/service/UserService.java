package com.boricori.service;

import com.boricori.dto.request.User.UserLoginRequest;
import com.boricori.dto.request.User.UserSignupRequest;
<<<<<<< HEAD
import com.boricori.dto.response.User.RankDtoResponse;
=======
import com.boricori.dto.response.User.UserLoginResponse;
>>>>>>> 95e5f312786646cd1408a8623fd6b6b074dd9dd7
import com.boricori.entity.User;

import java.util.List;


public interface UserService {

  User signup(UserSignupRequest request);

<<<<<<< HEAD
  int findUserScore(String email);
  List<RankDtoResponse> findAllRank();
=======
  UserLoginResponse login(UserLoginRequest request);

>>>>>>> 95e5f312786646cd1408a8623fd6b6b074dd9dd7
}
