package com.devouk.devouk_back.domain.common.exception;

import java.util.UUID;

public class AttemptNotFoundException extends BusinessException {
  public AttemptNotFoundException(UUID attemptUuid) {
    super(ErrorCode.ATTEMPT_NOT_FOUND, "시도를 찾을 수 없습니다. attemptUuid=" + attemptUuid);
  }
}
