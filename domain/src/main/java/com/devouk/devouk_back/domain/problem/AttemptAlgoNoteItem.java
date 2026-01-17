package com.devouk.devouk_back.domain.problem;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttemptAlgoNoteItem {
  private final String slug;
  private final String title;
  private final OffsetDateTime createdAt;
}
