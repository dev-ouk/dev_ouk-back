package com.devouk.devouk_back.domain.common.exception;

public class ProblemPreviewFetchException extends BusinessException {
  public ProblemPreviewFetchException(String message) {
    super(ErrorCode.PROBLEM_PREVIEW_FETCH_FAILED, message);
  }
}
