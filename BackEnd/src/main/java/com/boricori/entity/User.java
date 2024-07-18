package com.boricori.entity;


import lombok.Data;

@Data
public class User {

  String username;
  String email;
  int playtime;
  String password;
  String profilePic;
}
