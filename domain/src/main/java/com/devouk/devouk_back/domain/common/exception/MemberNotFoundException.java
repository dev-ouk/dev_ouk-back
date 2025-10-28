package com.devouk.devouk_back.domain.common.exception;

public class MemberNotFoundException extends BusinessException {
  public MemberNotFoundException(Long id) {
    super("Member not found with id: " + id);
  }
}
