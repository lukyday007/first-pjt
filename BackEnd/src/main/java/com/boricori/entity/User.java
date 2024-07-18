package com.boricori.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;


@Entity
@Getter
public class User {

  protected User() {
  }

  @Builder
  public User(String username, String email, int playtime, String password, String profilePic) {
    this.username = username;
    this.email = email;
    this.playtime = playtime;
    this.password = password;
    this.profilePic = profilePic;
  }

  @Id
  @GeneratedValue
  private Long userId;
  private String username;
  private String email;
  private int playtime = 0;
  private String password;
  private String profilePic = null;


  public void updateUser(String username, String password, String profilePic){
    this.username = username;
    this.password = password;
    this.profilePic = profilePic;
  }

  public void updatePlaytime(int playtime){
    this.playtime = playtime;
  }

}