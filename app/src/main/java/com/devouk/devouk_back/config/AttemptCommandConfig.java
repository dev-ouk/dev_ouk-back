package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.problem.AttemptCommandPort;
import com.devouk.devouk_back.infra.problem.AttemptJpaRepository;
import com.devouk.devouk_back.infra.problem.JpaAttemptCommandRepository;
import com.devouk.devouk_back.infra.problem.ProblemJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AttemptCommandConfig {

  @Bean
  public AttemptCommandPort attemptCommandPort(
      AttemptJpaRepository attemptJpaRepository, ProblemJpaRepository problemJpaRepository) {
    return new JpaAttemptCommandRepository(attemptJpaRepository, problemJpaRepository);
  }
}
