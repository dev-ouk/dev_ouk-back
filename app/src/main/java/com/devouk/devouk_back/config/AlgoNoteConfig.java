package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.algo_note.AlgoNoteSlugQueryPort;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteSlugService;
import com.devouk.devouk_back.domain.algo_note.SimpleSlugifier;
import com.devouk.devouk_back.domain.algo_note.Slugifier;
import com.devouk.devouk_back.infra.algo_note.AlgoNoteJpaRepository;
import com.devouk.devouk_back.infra.algo_note.JpaAlgoNoteSlugQueryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlgoNoteConfig {

  @Bean
  public AlgoNoteSlugQueryPort algoNoteSlugQueryPort(AlgoNoteJpaRepository algoNoteJpaRepository) {
    return new JpaAlgoNoteSlugQueryRepository(algoNoteJpaRepository);
  }

  @Bean
  public Slugifier algoNoteSlugifier() {
    return new SimpleSlugifier();
  }

  @Bean
  public AlgoNoteSlugService algoNoteSlugService(AlgoNoteSlugQueryPort port, Slugifier slugifier) {
    return new AlgoNoteSlugService(port, slugifier);
  }
}
