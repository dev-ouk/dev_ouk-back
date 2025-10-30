package com.devouk.devouk_back.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.devouk.devouk_back.infra.cors.CorsSourceBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;

class InfraWiringConfigTest {

  @Test
  void corsSourceBuilder_isBuiltFromAppProperties() {
    AppProperties props = new AppProperties();

    AppProperties.Cors cors = new AppProperties.Cors();
    cors.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));

    props.setCors(cors);

    InfraWiringConfig config = new InfraWiringConfig(props);

    CorsSourceBuilder bean = config.corsSourceBuilder();

    assertThat(bean.getAllowedOrigins())
        .containsExactly("http://localhost:3000", "http://localhost:5173");
  }
}
