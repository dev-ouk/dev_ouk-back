package com.devouk.devouk_back.domain.algo_note;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlgoNoteSlugPreview {
  private final String slug;
  private final boolean available;

  public static AlgoNoteSlugPreview of(String slug, boolean available) {
    return new AlgoNoteSlugPreview(slug, available);
  }
}
