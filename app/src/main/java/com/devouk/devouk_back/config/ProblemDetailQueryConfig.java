package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.problem.ProblemDetailQueryPort;
import com.devouk.devouk_back.infra.problem.AttemptJpaRepository;
import com.devouk.devouk_back.infra.problem.JpaProblemDetailQueryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProblemDetailQueryConfig {

  @Bean
  public ProblemDetailQueryPort problemDetailQueryPort(
      EntityManager em, AttemptJpaRepository attemptJpaRepository) {
    return new JpaProblemDetailQueryRepository(em, attemptJpaRepository);
  }
}
