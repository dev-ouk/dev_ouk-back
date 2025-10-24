package com.devouk.devouk_back.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DummyDomainTest {
  @Test
  void canInstantiate() {
    DummyDomain d = new DummyDomain();
    assertThat(d).isNotNull();
  }
}
