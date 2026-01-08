package com.devouk.devouk_back.domain.common.exception;

import java.util.List;

public class ProblemTagNotFoundException extends BusinessException {
  public ProblemTagNotFoundException(List<String> missingSlugs) {
    super(
        ErrorCode.PROBLEM_TAG_NOT_FOUND,
        "요청한 태그 슬러그를 찾을 수 없습니다: " + String.join(", ", missingSlugs));
  }
}
