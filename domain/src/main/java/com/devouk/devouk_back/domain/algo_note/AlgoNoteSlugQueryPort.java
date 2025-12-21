package com.devouk.devouk_back.domain.algo_note;

public interface AlgoNoteSlugQueryPort {
  boolean existsBySlug(String slug);
}
