package com.devouk.devouk_back.infra.cors;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class CorsSourceBuilderTest {

  @Test
  void getAllowedOrigins_returnsInjectedList() {
    var origins = List.of("http://localhost:3000", "http://localhost:5173");

    var builder = new CorsSourceBuilder(origins);

    assertThat(builder.getAllowedOrigins())
        .containsExactly("http://localhost:3000", "http://localhost:5173");
  }
}
