package com.devouk.devouk_back.domain.common.exception;

public class DuplicateMemberException extends BusinessException {
  public DuplicateMemberException(String email) {
    super(ErrorCode.DUPLICATE_MEMBER, "Member already exists with email: " + email);
  }
}
