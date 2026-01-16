package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.problem.AttemptAlgoNoteCommandPort;
import com.devouk.devouk_back.infra.algo_note.AlgoNoteJpaRepository;
import com.devouk.devouk_back.infra.problem.AttemptAlgoNoteLinkJpaRepository;
import com.devouk.devouk_back.infra.problem.AttemptJpaRepository;
import com.devouk.devouk_back.infra.problem.JpaAttemptAlgoNoteCommandRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AttemptAlgoNoteCommandConfig {

  @Bean
  public AttemptAlgoNoteCommandPort attemptAlgoNoteCommandPort(
      AttemptJpaRepository attemptRepo,
      AlgoNoteJpaRepository algoNoteRepo,
      AttemptAlgoNoteLinkJpaRepository linkRepo) {

    return new JpaAttemptAlgoNoteCommandRepository(attemptRepo, algoNoteRepo, linkRepo);
  }
}
