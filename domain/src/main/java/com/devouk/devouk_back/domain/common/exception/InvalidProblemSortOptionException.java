package com.devouk.devouk_back.domain.common.exception;

public class InvalidProblemSortOptionException extends BusinessException {
  public InvalidProblemSortOptionException(String value) {
    super(ErrorCode.INVALID_PROBLEM_SORT_OPTION, "지원하지 않는 정렬 옵션입니다. sort=" + value);
  }
}
