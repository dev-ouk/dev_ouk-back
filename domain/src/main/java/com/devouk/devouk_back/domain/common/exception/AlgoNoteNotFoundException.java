package com.devouk.devouk_back.domain.common.exception;

public class AlgoNoteNotFoundException extends BusinessException {
  public AlgoNoteNotFoundException(String slug) {
    super(ErrorCode.ALGO_NOTE_NOT_FOUND, "알고리즘 노트를 찾을 수 없습니다. slug=" + slug);
  }
}
