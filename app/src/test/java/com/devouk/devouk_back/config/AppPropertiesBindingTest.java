package com.devouk.devouk_back.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

class AppPropertiesBindingTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withUserConfiguration(TestConfig.class)
          .withPropertyValues(
              "app.name=devouk-back",
              "app.cors.allowed-origins[0]=http://localhost:3000",
              "app.cors.allowed-origins[1]=http://localhost:5173");

  @Test
  void bind_success() {
    contextRunner.run(
        ctx -> {
          assertThat(ctx).hasNotFailed();
          AppProperties props = ctx.getBean(AppProperties.class);
          assertThat(props.getName()).isEqualTo("devouk-back");
          assertThat(props.getCors().getAllowedOrigins())
              .containsExactly("http://localhost:3000", "http://localhost:5173");
        });
  }

  @Configuration
  @EnableConfigurationProperties(AppProperties.class)
  static class TestConfig {}
}
