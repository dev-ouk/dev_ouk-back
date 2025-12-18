package com.devouk.devouk_back.domain.common.exception;

public class InvalidAttemptVerdictException extends BusinessException {
  public InvalidAttemptVerdictException(String value) {
    super("지원하지 않는 시도 결과입니다. finalVerdict=" + value);
  }
}
