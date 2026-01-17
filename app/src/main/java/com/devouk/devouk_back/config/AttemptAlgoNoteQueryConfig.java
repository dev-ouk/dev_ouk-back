package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.problem.AttemptAlgoNoteQueryPort;
import com.devouk.devouk_back.infra.problem.AttemptAlgoNoteLinkJpaRepository;
import com.devouk.devouk_back.infra.problem.AttemptJpaRepository;
import com.devouk.devouk_back.infra.problem.JpaAttemptAlgoNoteQueryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AttemptAlgoNoteQueryConfig {

  @Bean
  public AttemptAlgoNoteQueryPort attemptAlgoNoteQueryPort(
      AttemptJpaRepository attemptRepo, AttemptAlgoNoteLinkJpaRepository linkRepo) {
    return new JpaAttemptAlgoNoteQueryRepository(attemptRepo, linkRepo);
  }
}
