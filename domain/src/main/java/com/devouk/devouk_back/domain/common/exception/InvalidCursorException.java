package com.devouk.devouk_back.domain.common.exception;

public class InvalidCursorException extends BusinessException {
  public InvalidCursorException() {
    super("잘못된 cursor 값입니다.");
  }
}
