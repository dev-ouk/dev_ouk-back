package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.activity.ActivityHeatmapQueryPort;
import com.devouk.devouk_back.infra.activity.JpaActivityHeatmapQueryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivityQueryConfig {
  @Bean
  public ActivityHeatmapQueryPort activityHeatmapQueryPort(EntityManager em) {
    return new JpaActivityHeatmapQueryRepository(em);
  }
}
