package com.devouk.devouk_back.domain.common.exception;

public class TaxonomyNotFoundException extends BusinessException {
  public TaxonomyNotFoundException(String code) {
    super("요청한 분류를 찾을 수 없습니다. code: " + code);
  }
}
