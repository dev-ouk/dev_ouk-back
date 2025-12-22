package com.devouk.devouk_back.domain.common.exception;

public class DuplicateAlgoNoteSlugException extends BusinessException {
  public DuplicateAlgoNoteSlugException(String slug) {
    super("이미 사용중인 slug 입니다. slug=" + slug);
  }
}
