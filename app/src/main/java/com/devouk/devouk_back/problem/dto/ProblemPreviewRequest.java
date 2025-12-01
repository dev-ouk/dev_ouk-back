package com.devouk.devouk_back.problem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProblemPreviewRequest {
  @NotBlank(message = "url은 비울 수 없습니다.")
  private String url;
}
