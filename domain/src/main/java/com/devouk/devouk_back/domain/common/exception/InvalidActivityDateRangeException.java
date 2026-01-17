package com.devouk.devouk_back.domain.common.exception;

public class InvalidActivityDateRangeException extends BusinessException {
  public InvalidActivityDateRangeException() {
    super(ErrorCode.INVALID_ACTIVITY_DATE_RANGE, "from 은 to 보다 클 수 없습니다.");
  }
}
