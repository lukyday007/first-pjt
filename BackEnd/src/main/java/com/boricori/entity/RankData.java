package com.boricori.entity;

import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@Getter
public class RankData {
  private int rank;
  private String username;
  private int score;
}
