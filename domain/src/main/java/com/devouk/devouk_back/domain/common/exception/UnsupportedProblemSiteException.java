package com.devouk.devouk_back.domain.common.exception;

public class UnsupportedProblemSiteException extends BusinessException {
  public UnsupportedProblemSiteException(String url) {
    super("지원하지 않는 문제 URL 입니다: " + url);
  }
}
