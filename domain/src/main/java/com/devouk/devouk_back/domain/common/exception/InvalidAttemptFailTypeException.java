package com.devouk.devouk_back.domain.common.exception;

public class InvalidAttemptFailTypeException extends BusinessException {
  public InvalidAttemptFailTypeException(String value) {
    super(ErrorCode.INVALID_ATTEMPT_FAIL_TYPE, "지원하지 않는 실패 유형입니다. failType=" + value);
  }
}
