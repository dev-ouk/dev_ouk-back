package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.algo_note.AlgoNoteCommandPort;
import com.devouk.devouk_back.infra.algo_note.AlgoNoteJpaRepository;
import com.devouk.devouk_back.infra.algo_note.JpaAlgoNoteCommandRepository;
import com.devouk.devouk_back.infra.taxonomy.TermJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlgoNoteCommandConfig {

  @Bean
  public AlgoNoteCommandPort algoNoteCommandPort(
      AlgoNoteJpaRepository algoNoteJpaRepository, TermJpaRepository termJpaRepository) {
    return new JpaAlgoNoteCommandRepository(algoNoteJpaRepository, termJpaRepository);
  }
}
