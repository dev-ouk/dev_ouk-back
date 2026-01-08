package com.devouk.devouk_back.attempt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;

@Getter
public class AttemptCreateRequest {

  @NotBlank(message = "site 는 비울 수 없습니다.")
  private String site;

  @NotBlank(message = "siteProblemId 는 비울 수 없습니다.")
  @Size(max = 64, message = "siteProblemId 는 64자 이하여야 합니다.")
  private String siteProblemId;

  @NotNull(message = "timeSpent 는 비울 수 없습니다.")
  @Min(value = 0, message = "timeSpent 는 0 이상이어야 합니다.")
  private Integer timeSpent;

  @NotBlank(message = "language 는 비울 수 없습니다.")
  private String language;

  private String notes;

  @NotBlank(message = "verdict 는 비울 수 없습니다.")
  private String verdict;

  private String code;

  private OffsetDateTime attemptedAt;

  private String failType;
  private String failDetail;
  private String solution;

  private OffsetDateTime nextReviewAt;

  public void setSite(String site) {
    this.site = site;
  }

  public void setSiteProblemId(String siteProblemId) {
    this.siteProblemId = siteProblemId;
  }

  public void setTimeSpent(Integer timeSpent) {
    this.timeSpent = timeSpent;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setVerdict(String verdict) {
    this.verdict = verdict;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setAttemptedAt(OffsetDateTime attemptedAt) {
    this.attemptedAt = attemptedAt;
  }

  public void setFailType(String failType) {
    this.failType = failType;
  }

  public void setFailDetail(String failDetail) {
    this.failDetail = failDetail;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }

  public void setNextReviewAt(OffsetDateTime nextReviewAt) {
    this.nextReviewAt = nextReviewAt;
  }
}
