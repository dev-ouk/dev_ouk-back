package com.devouk.devouk_back.domain.algo_note;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlgoNoteSlugService {
  private final AlgoNoteSlugQueryPort slugQueryPort;
  private final Slugifier slugifier;

  public AlgoNoteSlugPreview preview(String title) {
    String slug = slugifier.slugify(title);
    boolean available = !slugQueryPort.existsBySlug(slug);
    return AlgoNoteSlugPreview.of(slug, available);
  }
}
