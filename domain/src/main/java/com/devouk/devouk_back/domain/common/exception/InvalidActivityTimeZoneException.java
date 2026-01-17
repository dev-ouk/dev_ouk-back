package com.devouk.devouk_back.domain.common.exception;

public class InvalidActivityTimeZoneException extends BusinessException {
  public InvalidActivityTimeZoneException(String value) {
    super(ErrorCode.INVALID_ACTIVITY_TIME_ZONE, "지원하지 않는 timeZone 값입니다. timeZone=" + value);
  }
}
