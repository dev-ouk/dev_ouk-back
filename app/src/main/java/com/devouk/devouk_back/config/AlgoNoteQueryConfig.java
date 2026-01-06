package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.algo_note.AlgoNoteDetailQueryPort;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteListQueryPort;
import com.devouk.devouk_back.infra.algo_note.AlgoNoteJpaRepository;
import com.devouk.devouk_back.infra.algo_note.JpaAlgoNoteDetailQueryRepository;
import com.devouk.devouk_back.infra.algo_note.JpaAlgoNoteListQueryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlgoNoteQueryConfig {

  @Bean
  public AlgoNoteListQueryPort algoNoteListQueryPort(EntityManager em) {
    return new JpaAlgoNoteListQueryRepository(em);
  }

  @Bean
  public AlgoNoteDetailQueryPort algoNoteDetailQueryPort(AlgoNoteJpaRepository repo) {
    return new JpaAlgoNoteDetailQueryRepository(repo);
  }
}
