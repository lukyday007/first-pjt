package com.boricori.repository.userRepo;

import com.boricori.dto.request.gameroom.PlayerInfoRequest;
import com.boricori.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserRepositoryImplTest {

  @Autowired
  UserRepositoryImpl userRepositoryImpl;

  @Test
  public void getUserTest() {
    List<PlayerInfoRequest> userList = new ArrayList<>();
    userList.add(new PlayerInfoRequest("길동", "1234@gmail.com"));
    userList.add(new PlayerInfoRequest("둘리", "5678@gmail.com"));
    userList.add(new PlayerInfoRequest("또치", "5678@gmail.com"));

    List<User> list = userRepositoryImpl.getUserList(userList);
    System.out.println(list.size());
    for (User user : list) {
      System.out.println(user.getUserId() + " " + user.getUsername() + " " + user.getEmail());
    }
  }


}