package com.boricori.service;

import com.boricori.dto.request.User.UserSignupRequest;
import com.boricori.dto.response.User.RankDtoResponse;
import com.boricori.entity.User;

import java.util.List;


public interface UserService {

  User signup(UserSignupRequest request);

  int findUserScore(String email);
  List<RankDtoResponse> findAllRank();
}
