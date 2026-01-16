package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.problem.AttemptHistoryQueryPort;
import com.devouk.devouk_back.infra.problem.AttemptJpaRepository;
import com.devouk.devouk_back.infra.problem.JpaAttemptHistoryQueryRepository;
import com.devouk.devouk_back.infra.problem.ProblemJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AttemptQueryConfig {

  @Bean
  public AttemptHistoryQueryPort attemptHistoryQueryPort(
      AttemptJpaRepository attemptJpaRepository, ProblemJpaRepository problemJpaRepository) {
    return new JpaAttemptHistoryQueryRepository(attemptJpaRepository, problemJpaRepository);
  }
}
