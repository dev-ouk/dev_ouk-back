package com.devouk.devouk_back.domain.common.exception;

import java.util.List;

public class AlgoNoteSlugsNotFoundException extends BusinessException {
  public AlgoNoteSlugsNotFoundException(List<String> missingSlugs) {
    super(
        ErrorCode.ALGO_NOTE_SLUGS_NOT_FOUND,
        "요청한 알고리즘 노트를 찾을 수 없습니다: " + String.join(", ", missingSlugs));
  }
}
