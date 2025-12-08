package com.devouk.devouk_back.domain.problem;

import static org.assertj.core.api.Assertions.assertThat;

import com.devouk.devouk_back.domain.common.exception.ProblemPreviewFetchException;
import com.devouk.devouk_back.domain.common.exception.UnsupportedProblemSiteException;
import org.junit.jupiter.api.Test;

class ProblemPreviewExceptionsTest {

  @Test
  void exceptions_haveMessage() {
    ProblemPreviewFetchException ex1 = new ProblemPreviewFetchException("msg");
    UnsupportedProblemSiteException ex2 = new UnsupportedProblemSiteException("url");

    assertThat(ex1).hasMessage("msg");
    assertThat(ex2.getMessage()).contains("url");
  }
}
