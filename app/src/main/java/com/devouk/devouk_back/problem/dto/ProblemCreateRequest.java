package com.devouk.devouk_back.problem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

@Getter
public class ProblemCreateRequest {

  @NotBlank(message = "site 는 비울 수 없습니다.")
  private String site;

  @NotBlank(message = "siteProblemId 는 비울 수 없습니다.")
  private String siteProblemId;

  @NotBlank(message = "title 은 비울 수 없습니다.")
  private String title;

  @NotBlank(message = "url 은 비울 수 없습니다.")
  private String url;

  @NotNull(message = "difficulty 는 비울 수 없습니다.")
  private Integer difficulty;

  private List<@NotBlank(message = "tagSlugs 항목은 비울 수 없습니다.") String> tagSlugs;

  public void setSite(String site) {
    this.site = site;
  }

  public void setSiteProblemId(String siteProblemId) {
    this.siteProblemId = siteProblemId;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setDifficulty(Integer difficulty) {
    this.difficulty = difficulty;
  }

  public void setTagSlugs(List<String> tagSlugs) {
    this.tagSlugs = tagSlugs;
  }
}
