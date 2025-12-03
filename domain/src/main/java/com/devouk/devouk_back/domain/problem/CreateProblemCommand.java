package com.devouk.devouk_back.domain.problem;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateProblemCommand {

  private final ProblemSite site;
  private final String siteProblemId;
  private final String title;
  private final String url;
  private final Integer difficulty;
  private final List<String> tagSlugs;
}
