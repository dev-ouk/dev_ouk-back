package com.devouk.devouk_back.domain.common.exception;

public class MemberNotFoundException extends BusinessException {
  public MemberNotFoundException(Long id) {
    super(ErrorCode.MEMBER_NOT_FOUND, "Member not found with id: " + id);
  }
}
