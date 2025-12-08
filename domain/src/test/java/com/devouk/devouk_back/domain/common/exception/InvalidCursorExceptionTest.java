package com.devouk.devouk_back.domain.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InvalidCursorExceptionTest {

  @Test
  void message_isFixedKoreanMessage() {
    InvalidCursorException ex = new InvalidCursorException();

    assertThat(ex.getMessage()).isEqualTo("잘못된 cursor 값입니다.");
  }
}
