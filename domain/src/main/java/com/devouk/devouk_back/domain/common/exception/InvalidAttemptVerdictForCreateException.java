package com.devouk.devouk_back.domain.common.exception;

public class InvalidAttemptVerdictForCreateException extends BusinessException {
  public InvalidAttemptVerdictForCreateException(String value) {
    super(ErrorCode.INVALID_ATTEMPT_VERDICT_FOR_CREATE, "지원하지 않는 시도 결과입니다. verdict=" + value);
  }
}
