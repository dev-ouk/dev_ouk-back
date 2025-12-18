package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.problem.ProblemAttemptQueryPort;
import com.devouk.devouk_back.domain.problem.ProblemCandidateQueryPort;
import com.devouk.devouk_back.infra.problem.AttemptJpaRepository;
import com.devouk.devouk_back.infra.problem.JpaProblemAttemptQueryRepository;
import com.devouk.devouk_back.infra.problem.JpaProblemCandidateQueryRepository;
import com.devouk.devouk_back.infra.problem.ProblemJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProblemQueryConfig {

  @Bean
  public ProblemCandidateQueryPort problemCandidateQueryPort(
      ProblemJpaRepository problemJpaRepository, AttemptJpaRepository attemptJpaRepository) {
    return new JpaProblemCandidateQueryRepository(problemJpaRepository, attemptJpaRepository);
  }

  @Bean
  public ProblemAttemptQueryPort problemAttemptQueryPort(EntityManager entityManager) {
    return new JpaProblemAttemptQueryRepository(entityManager);
  }
}
