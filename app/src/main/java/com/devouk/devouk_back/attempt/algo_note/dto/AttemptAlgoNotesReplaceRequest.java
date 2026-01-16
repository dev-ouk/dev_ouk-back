package com.devouk.devouk_back.attempt.algo_note.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;

@Getter
public class AttemptAlgoNotesReplaceRequest {

  @NotNull(message = "algoNoteSlugs 는 비울 수 없습니다.")
  private List<
          @NotBlank(message = "algoNoteSlugs 항목은 비울 수 없습니다.")
          @Size(max = 200, message = "algoNoteSlugs 항목은 200자 이하여야 합니다.") String>
      algoNoteSlugs;

  public void setAlgoNoteSlugs(List<String> algoNoteSlugs) {
    this.algoNoteSlugs = algoNoteSlugs;
  }
}
