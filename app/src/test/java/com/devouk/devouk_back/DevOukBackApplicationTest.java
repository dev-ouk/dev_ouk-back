package com.devouk.devouk_back;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

public class DevOukBackApplicationTest {
  @Test
  void main_runs_without_exceptions() {
    // 내장 톰캣 뜨지 않도록 non-web 모드로 실행
    String[] args =
        new String[] {
          "--spring.main.web-application-type=none",
          // 필요하면 다른 부트 파라미터도 여기에 추가 가능
        };
    assertDoesNotThrow(() -> DevOukBackApplication.main(args));
  }
}
