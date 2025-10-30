package com.devouk.devouk_back.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

public class AppPropertiesValidationFailTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withUserConfiguration(TestConfig.class)
          .withPropertyValues("app.cors.allowed-origins[0]=http://localhost:3000");

  @Test
  void bind_fail_when_required_missing() {
    contextRunner.run(
        ctx -> {
          assertThat(ctx).hasFailed();

          Throwable failure = ctx.getStartupFailure();
          assertThat(failure).isNotNull();

          Throwable root = getRootCause(failure);
          assertThat(root.getMessage()).contains("app.name").contains("비울 수 없습니다");
        });
  }

  private Throwable getRootCause(Throwable t) {
    Throwable result = t;
    while (result.getCause() != null) {
      result = result.getCause();
    }
    return result;
  }

  @Configuration
  @EnableConfigurationProperties(AppProperties.class)
  static class TestConfig {}
}
