package com.devouk.devouk_back.domain.common.exception;

public class InvalidAttemptVerdictException extends BusinessException {
  public InvalidAttemptVerdictException(String value) {
    super(ErrorCode.INVALID_ATTEMPT_VERDICT, "지원하지 않는 시도 결과입니다. finalVerdict=" + value);
  }
}
