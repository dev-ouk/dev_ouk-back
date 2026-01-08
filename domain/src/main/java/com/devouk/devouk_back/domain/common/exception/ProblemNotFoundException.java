package com.devouk.devouk_back.domain.common.exception;

import com.devouk.devouk_back.domain.problem.ProblemSite;

public class ProblemNotFoundException extends BusinessException {
  public ProblemNotFoundException(ProblemSite site, String siteProblemId) {
    super(
        ErrorCode.PROBLEM_NOT_FOUND,
        "문제를 찾을 수 없습니다. site=" + site + ", siteProblemId=" + siteProblemId);
  }
}
