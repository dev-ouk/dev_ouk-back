package com.devouk.devouk_back.domain.common.exception;

public class InvalidProblemSiteException extends BusinessException {
  public InvalidProblemSiteException(String site) {
    super(ErrorCode.INVALID_PROBLEM_SITE, "지원하지 않는 문제 사이트입니다. site=" + site);
  }
}
