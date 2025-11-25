package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.taxonomy.TaxonomyQueryPort;
import com.devouk.devouk_back.infra.taxonomy.JpaTaxonomyQueryRepository;
import com.devouk.devouk_back.infra.taxonomy.TaxonomyJpaRepository;
import com.devouk.devouk_back.infra.taxonomy.TermJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaxonomyConfig {

  @Bean
  public TaxonomyQueryPort taxonomyQueryPort(
      TermJpaRepository termJpaRepository, TaxonomyJpaRepository taxonomyJpaRepository) {
    return new JpaTaxonomyQueryRepository(termJpaRepository, taxonomyJpaRepository);
  }
}
