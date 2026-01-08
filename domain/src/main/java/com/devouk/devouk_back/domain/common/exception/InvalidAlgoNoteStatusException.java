package com.devouk.devouk_back.domain.common.exception;

public class InvalidAlgoNoteStatusException extends BusinessException {
  public InvalidAlgoNoteStatusException(String value) {
    super(ErrorCode.INVALID_ALGO_NOTE_STATUS, "지원하지 않는 알고리즘 노트 상태입니다. status=" + value);
  }
}
