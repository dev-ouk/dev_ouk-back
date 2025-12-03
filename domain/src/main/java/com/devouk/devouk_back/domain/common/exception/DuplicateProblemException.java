package com.devouk.devouk_back.domain.common.exception;

import com.devouk.devouk_back.domain.problem.ProblemSite;

public class DuplicateProblemException extends BusinessException {
  public DuplicateProblemException(ProblemSite site, String siteProblemId) {
    super("이미 등록된 문제입니다. site=" + site + ", siteProblemId=" + siteProblemId);
  }
}
