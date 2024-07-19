package com.boricori.service;

import com.boricori.dto.request.User.UserSignupRequest;
import com.boricori.entity.User;
import com.boricori.repository.userRepo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

  @Autowired
  UserRepository userRepo;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Override
  public User signup(UserSignupRequest request) {
    User newUser = User.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .username(request.getUsername())
        .build();

    User signedUp = userRepo.save(newUser);
    return signedUp;
  }
}
