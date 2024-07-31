package com.boricori.exception;

public class TokenExpiredException extends RuntimeException{
      String message = "토큰이 만료되었습니다.";

      @Override
       public String getMessage(){
            return message;
      }
}
