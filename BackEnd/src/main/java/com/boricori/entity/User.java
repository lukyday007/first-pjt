package com.boricori.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;


@Entity
@Getter
@Table(name = "user")
public class User {

  public User() {
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;
  private String username;
  private String email;
//  @ColumnDefault("0")
  private int playtime = 0;
  private String password;
//  @ColumnDefault("")
  private String profilePic = "";
//  @ColumnDefault("0")
  private int scores = 0;


  public void updateUser(String username, String password, String profilePic){
    this.username = username;
    this.password = password;
    this.profilePic = profilePic;
  }

  public void updatePlaytime(int playtime){
    this.playtime = playtime;
  }

}