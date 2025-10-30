package com.devouk.devouk_back.config;

import com.devouk.devouk_back.infra.cors.CorsSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfraWiringConfig {

  private final AppProperties appProperties;

  public InfraWiringConfig(AppProperties appProperties) {
    this.appProperties = appProperties;
  }

  @Bean
  public CorsSourceBuilder corsSourceBuilder() {
    return new CorsSourceBuilder(appProperties.getCors().getAllowedOrigins());
  }
}
