package com.devouk.devouk_back.infra.problem;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AttemptEntityTest {

  @Test
  void defaultConstructor_initializesFieldsToNull() {
    // 같은 패키지라 protected 생성자 호출 가능
    AttemptEntity entity = new AttemptEntity();

    assertThat(entity.getId()).isNull();
    assertThat(entity.getAttemptUuid()).isNull();
    assertThat(entity.getProblem()).isNull();
    assertThat(entity.getVerdict()).isNull();
    assertThat(entity.getAttemptedAt()).isNull();
  }
}
