package com.devouk.devouk_back.problem.dto;

import com.devouk.devouk_back.domain.problem.Problem;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemResponse {

  private Long problemId;
  private String site;
  private String siteProblemId;
  private String title;
  private String url;
  private Integer difficulty;
  private List<String> tagSlugs;

  public static ProblemResponse from(Problem problem) {
    return new ProblemResponse(
        problem.getId(),
        problem.getSite().name(),
        problem.getSiteProblemId(),
        problem.getTitle(),
        problem.getUrl(),
        problem.getDifficulty(),
        problem.getTagSlugs());
  }
}
