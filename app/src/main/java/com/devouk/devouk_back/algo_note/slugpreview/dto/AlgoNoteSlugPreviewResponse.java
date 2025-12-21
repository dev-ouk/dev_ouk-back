package com.devouk.devouk_back.algo_note.slugpreview.dto;

import com.devouk.devouk_back.domain.algo_note.AlgoNoteSlugPreview;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlgoNoteSlugPreviewResponse {
  private final String slug;
  private final boolean available;

  public static AlgoNoteSlugPreviewResponse from(AlgoNoteSlugPreview preview) {
    return new AlgoNoteSlugPreviewResponse(preview.getSlug(), preview.isAvailable());
  }
}
