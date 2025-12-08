package com.devouk.devouk_back.domain.problem;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemCandidateQuery {

  private final String q;
  private final List<ProblemSite> sites;
  private final int size;
  private final Long cursorProblemId;
}
