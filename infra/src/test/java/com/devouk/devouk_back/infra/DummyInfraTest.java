package com.devouk.devouk_back.infra;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DummyInfraTest {
  @Test
  void canInstantiate() {
    DummyInfra d = new DummyInfra();
    assertThat(d).isNotNull();
  }
}
