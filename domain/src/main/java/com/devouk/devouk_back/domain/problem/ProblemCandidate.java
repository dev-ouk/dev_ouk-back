package com.devouk.devouk_back.domain.problem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemCandidate {

  private final ProblemSite site;
  private final String siteProblemId;
  private final String title;
  private final Integer difficulty;
  private final ProblemLastAttemptSummary lastAttempt;
}
