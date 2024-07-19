package com.boricori.service;

import com.boricori.dto.request.User.UserSignupRequest;
import com.boricori.dto.response.User.RankDtoResponse;
import com.boricori.entity.User;
import com.boricori.repository.userRepo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

  @Autowired
  UserRepository userRepo;

  @Override
  public User signup(UserSignupRequest request) {
    User newUser = User.builder()
        .email(request.getEmail())
        .password(request.getPassword())
        .username(request.getUsername())
        .build();

    User signedUp = userRepo.save(newUser);
    return signedUp;
  }

  @Override
  public int findUserScore(String email){
    return userRepo.findUserRankingByEmail(email);
  }

  @Override
  public List<RankDtoResponse> findAllRank(){
    List<User> RankAll = userRepo.findAllByOrderByScoresAsc();
    List<RankDtoResponse> rankList = new ArrayList<>();
    for (int i=0; i<RankAll.size(); i++) {
      rankList.add(new RankDtoResponse(i+1, RankAll.get(i)));
    }

    return rankList;
  }
}
