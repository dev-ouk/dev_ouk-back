package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.problem.ProblemCommandPort;
import com.devouk.devouk_back.infra.problem.JpaProblemCommandRepository;
import com.devouk.devouk_back.infra.problem.ProblemJpaRepository;
import com.devouk.devouk_back.infra.taxonomy.TermJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProblemCommandConfig {

  @Bean
  public ProblemCommandPort problemCommandPort(
      ProblemJpaRepository problemJpaRepository, TermJpaRepository termJpaRepository) {
    return new JpaProblemCommandRepository(problemJpaRepository, termJpaRepository);
  }
}
