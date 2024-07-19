package com.boricori.service;

import com.boricori.dto.request.User.UserLoginRequest;
import com.boricori.dto.request.User.UserSignupRequest;
import com.boricori.dto.response.User.UserLoginResponse;
import com.boricori.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {

  User signup(UserSignupRequest request);

  String login(UserLoginRequest request);

}
