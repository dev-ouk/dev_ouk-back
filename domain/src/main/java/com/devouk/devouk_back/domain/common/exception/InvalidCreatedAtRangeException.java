package com.devouk.devouk_back.domain.common.exception;

public class InvalidCreatedAtRangeException extends BusinessException {
  public InvalidCreatedAtRangeException() {
    super(ErrorCode.INVALID_CREATED_AT_RANGE, "createdAtFrom 은 createdAtTo 보다 클 수 없습니다.");
  }
}
