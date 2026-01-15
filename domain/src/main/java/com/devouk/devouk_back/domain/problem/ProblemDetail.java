package com.devouk.devouk_back.domain.problem;

import com.devouk.devouk_back.domain.taxonomy.Term;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemDetail {

  private final ProblemSite site;
  private final String siteProblemId;
  private final String title;
  private Integer difficulty;
  private final String url;

  private final ProblemLastAttemptSummary lastAttempt;

  private final Map<String, List<Term>> taxonomies;
}
