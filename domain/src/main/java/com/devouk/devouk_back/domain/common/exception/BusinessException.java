package com.devouk.devouk_back.domain.common.exception;

public class BusinessException extends RuntimeException {

  protected BusinessException(String message) {
    super(message);
  }
}
