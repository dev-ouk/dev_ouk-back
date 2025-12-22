package com.devouk.devouk_back.domain.common.exception;

import java.util.List;

public class AlgoNoteTagNotFoundException extends BusinessException {
  public AlgoNoteTagNotFoundException(List<String> missingSlugs) {
    super("요청한 태그 슬러그를 찾을 수 없습니다: " + String.join(", ", missingSlugs));
  }
}
