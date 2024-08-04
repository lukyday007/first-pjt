package com.boricori.exception;

public class TokenFormatException extends RuntimeException{
      private String message =  "Authorization header missing or not Bearer";
  @Override
  public String getMessage(){
    return message;
  }
}
