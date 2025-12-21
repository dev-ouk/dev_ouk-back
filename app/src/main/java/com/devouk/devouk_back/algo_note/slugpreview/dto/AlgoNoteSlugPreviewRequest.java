package com.devouk.devouk_back.algo_note.slugpreview.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AlgoNoteSlugPreviewRequest {

  @NotBlank(message = "title 은 비울 수 없습니다.")
  @Size(max = 200, message = "title 은 200자 이하여야 합니다.")
  private String title;

  public void setTitle(String title) {
    this.title = title;
  }
}
