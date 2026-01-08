package com.devouk.devouk_back.domain.common.exception;

public class DuplicateAlgoNoteSlugException extends BusinessException {
  public DuplicateAlgoNoteSlugException(String slug) {
    super(ErrorCode.DUPLICATE_ALGO_NOTE_SLUG, "이미 사용중인 slug 입니다. slug=" + slug);
  }
}
