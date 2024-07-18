package com.boricori.entity;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class RankData {
  int rank;
  String username;
  int score;
}
