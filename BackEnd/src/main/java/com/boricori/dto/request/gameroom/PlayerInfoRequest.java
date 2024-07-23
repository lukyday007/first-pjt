package com.boricori.dto.request.gameroom;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerInfoRequest {

  private String username;
  private String email;
}
