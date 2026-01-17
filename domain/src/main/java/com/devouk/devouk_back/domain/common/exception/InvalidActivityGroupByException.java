package com.devouk.devouk_back.domain.common.exception;

public class InvalidActivityGroupByException extends BusinessException {
  public InvalidActivityGroupByException(String value) {
    super(ErrorCode.INVALID_ACTIVITY_GROUP_BY, "지원하지 않는 groupBy 값입니다. groupBy=" + value);
  }
}
