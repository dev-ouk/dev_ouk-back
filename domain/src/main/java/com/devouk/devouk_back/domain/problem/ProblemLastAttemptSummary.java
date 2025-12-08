package com.devouk.devouk_back.domain.problem;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemLastAttemptSummary {
  private final AttemptVerdict verdict;
  private final OffsetDateTime attemptedAt;
}
