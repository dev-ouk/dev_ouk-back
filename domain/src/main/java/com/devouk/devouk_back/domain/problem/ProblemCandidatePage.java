package com.devouk.devouk_back.domain.problem;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemCandidatePage {
  private final List<ProblemCandidate> items;
  private final int size;
  private final boolean hasNext;
  private final Long nextCursorProblemId;
}
