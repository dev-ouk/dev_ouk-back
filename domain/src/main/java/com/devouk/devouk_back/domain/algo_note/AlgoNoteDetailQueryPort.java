package com.devouk.devouk_back.domain.algo_note;

import java.util.Optional;

public interface AlgoNoteDetailQueryPort {
  Optional<AlgoNoteDetail> findBySlug(String slug);
}
