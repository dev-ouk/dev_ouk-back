package com.devouk.devouk_back.domain.common.exception;

public class InvalidCursorException extends BusinessException {
  public InvalidCursorException() {
    super(ErrorCode.INVALID_CURSOR, "잘못된 cursor 값입니다.");
  }
}
