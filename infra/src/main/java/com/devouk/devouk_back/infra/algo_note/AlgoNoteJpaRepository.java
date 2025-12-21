package com.devouk.devouk_back.infra.algo_note;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlgoNoteJpaRepository extends JpaRepository<AlgoNoteEntity, Long> {
  boolean existsBySlug(String slug);
}
