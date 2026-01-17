package com.devouk.devouk_back.domain.common.exception;

public class InvalidActivityTypeException extends BusinessException {
  public InvalidActivityTypeException(String value) {
    super(ErrorCode.INVALID_ACTIVITY_TYPE, "지원하지 않는 활동 타입입니다. type=" + value);
  }
}
