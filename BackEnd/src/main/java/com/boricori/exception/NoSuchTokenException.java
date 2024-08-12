package com.boricori.exception;

public class NoSuchTokenException extends RuntimeException{
    private String message = "Invalid JWT Token";
  @Override
  public String getMessage(){
    return message;
  }
}
