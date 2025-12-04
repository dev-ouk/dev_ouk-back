package com.devouk.devouk_back.domain.problem;

import static org.assertj.core.api.Assertions.assertThat;

import com.devouk.devouk_back.domain.common.exception.DuplicateProblemException;
import com.devouk.devouk_back.domain.common.exception.InvalidProblemSiteException;
import com.devouk.devouk_back.domain.common.exception.ProblemTagNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProblemDomainBusinessExceptionsTest {

  @Test
  void duplicateProblemException_containsSiteAndIdInMessage() {
    DuplicateProblemException ex = new DuplicateProblemException(ProblemSite.BAEKJOON, "1000");

    assertThat(ex.getMessage()).contains("BAEKJOON").contains("1000");
  }

  @Test
  void problemTagNotFoundException_containsMissingSlugsInMessage() {
    ProblemTagNotFoundException ex = new ProblemTagNotFoundException(List.of("impl", "math"));

    assertThat(ex.getMessage()).contains("impl").contains("math");
  }

  @Test
  void invalidProblemSiteException_containsSiteInMessage() {
    InvalidProblemSiteException ex = new InvalidProblemSiteException("HACKERRANK");

    assertThat(ex.getMessage()).contains("HACKERRANK");
  }
}
