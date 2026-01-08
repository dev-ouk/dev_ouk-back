package com.devouk.devouk_back.domain.common.exception;

public class InvalidAttemptLanguageException extends BusinessException {
  public InvalidAttemptLanguageException(String value) {
    super(ErrorCode.INVALID_ATTEMPT_LANGUAGE, "지원하지 않는 언어입니다. language=" + value);
  }
}
