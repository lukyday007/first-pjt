package com.boricori.dto.response.User;

import com.boricori.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "유저 정보")
public class UserResponse{
    @Schema(description = "유저 닉네임", example = "유저입니당")
    String username;
    @Schema(description = "유저 이메일", example = "example@gmail.com")
    String email;
    @Schema(description = "총 게임 플레이 시간", example = "130")
    int playTime;
    @Schema(description = "프로필 사진 URL")
    String profilePic;


  public static UserResponse of(User user) {
    UserResponse res = new UserResponse();
    res.setUsername(user.getUsername());
    res.setEmail(user.getEmail());
    res.setPlayTime(user.getPlaytime());
    res.setProfilePic(user.getProfilePic());
    return res;
  }
}
