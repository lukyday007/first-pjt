package com.boricori.game;

import com.boricori.entity.User;
import com.boricori.util.UserCircularLinkedList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GameManager {

  private static GameManager gameManager = new GameManager();
  public static HashMap<Long, UserCircularLinkedList> catchableList = new HashMap<>();

  public static GameManager getGameManager() {
    if (gameManager == null) {
      gameManager = new GameManager();
    }
    return gameManager;
  }

  public void shuffleUsers(List<User> users) {
    Collections.shuffle(users);
  }

  public UserCircularLinkedList makeUserCatchableList(List<User> users) {
    return new UserCircularLinkedList(users);
  }


}
