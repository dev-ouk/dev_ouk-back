package com.devouk.devouk_back.domain.problem;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAttemptCommand {

  private final ProblemSite site;
  private final String siteProblemId;

  private final int timeSpent;
  private final AttemptLanguage language;

  private final String notes;
  private final AttemptVerdict verdict;

  private final String code;

  private final OffsetDateTime attemptedAt;

  private final AttemptFailType failType;
  private final String failDetail;

  private final String solution;

  private final OffsetDateTime nextReviewAt;
}
