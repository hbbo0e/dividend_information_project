package com.example.dividence.exception.impl;

import com.example.dividence.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class IdIncorrectException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "존재하지 않는 아이디입니다.";
  }
}
