package com.boricori.dto.response;

import lombok.Data;

@Data
public class KakaoToken {
  // ID 토큰을 발급한 인증 기관 정보
  //https://kauth.kakao.com로 고정
  private String iss;

  // ID 토큰이 발급된 앱의 앱 키
  //인가 코드 받기 요청 시 client_id에 전달된 앱 키
  //Kakao SDK를 통한 카카오 로그인의 경우, 해당 SDK 초기화 시 사용된 앱 키
  private String aud;

  //ID 토큰에 해당하는 사용자의 회원번호
  private String sub;

  //ID 토큰 발급 또는 갱신 시각, UNIX 타임스탬프(Timestamp)
  private int iat;

  //ID 토큰 발급 또는 갱신 시각, UNIX 타임스탬프(Timestamp)
  private int exp;

  //	사용자가 카카오 로그인을 통해 인증을 완료한 시각, UNIX 타임스탬프(Timestamp)
  private int auth_time;

  //인가 코드 받기 요청 시 전달한 nonce 값과 동일한 값
  //ID 토큰 유효성 검증 시 사용
  private String none;

  // 닉네임
  //사용자 정보 가져오기의 kakao_account.profile.nickname에 해당
  private String nickname;

  // 프로필 미리보기 이미지 URL
  //110px * 110px 또는 100px * 100px
  //사용자 정보 가져오기의 kakao_account.profile.thumbnail_image_url에 해당
  private String picture;

  // 카카오계정 대표 이메일
  //사용자 정보 가져오기의 kakao_account.email에 해당
  private String email;

}
