package com.devouk.devouk_back.domain.problem;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemAttemptQuery {

  private final String q;
  private final List<ProblemSite> sites;

  private final Integer difficultyMin;
  private final Integer difficultyMax;

  private final List<String> tagSlugs;

  private final AttemptVerdict finalVerdict;

  private final OffsetDateTime attemptedFrom;
  private final OffsetDateTime attemptedTo;

  private final ProblemSortOption sort;

  private final int size;

  private final OffsetDateTime cursorAttemptedAt;
  private final Integer cursorDifficulty;
  private final Long cursorProblemId;
}
