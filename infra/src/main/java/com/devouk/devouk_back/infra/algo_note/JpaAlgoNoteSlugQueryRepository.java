package com.devouk.devouk_back.infra.algo_note;

import com.devouk.devouk_back.domain.algo_note.AlgoNoteSlugQueryPort;

public class JpaAlgoNoteSlugQueryRepository implements AlgoNoteSlugQueryPort {

  private final AlgoNoteJpaRepository repo;

  public JpaAlgoNoteSlugQueryRepository(AlgoNoteJpaRepository repo) {
    this.repo = repo;
  }

  @Override
  public boolean existsBySlug(String slug) {
    return repo.existsBySlug(slug);
  }
}
