package com.devouk.devouk_back.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.devouk.devouk_back.domain.problem.ProblemCommandPort;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class ProblemCommandConfigTest {

  @Test
  void problemCommandPort_returnsJpaImplementation() throws Exception {
    ProblemCommandConfig config = new ProblemCommandConfig();

    // 1) problemCommandPort 메서드 리플렉션으로 찾기
    Method method = null;
    for (Method m : ProblemCommandConfig.class.getDeclaredMethods()) {
      if (m.getName().equals("problemCommandPort")) {
        method = m;
        break;
      }
    }
    assertThat(method).isNotNull();

    // 파라미터 타입은 2개(ProblemJpaRepository, TermJpaRepository)인지만 확인
    Class<?>[] paramTypes = method.getParameterTypes();
    assertThat(paramTypes).hasSize(2);

    // 2) 인자로 null 두 개 넘겨서 호출 (실제 bean 생성 시에는 Spring이 주입할 것)
    Object[] args = new Object[] {null, null};
    Object port = method.invoke(config, args);

    // 3) 리턴 타입이 ProblemCommandPort 구현체인지, 그리고
    //    com.devouk.devouk_back.infra.problem.JpaProblemCommandRepository 인지 확인
    assertThat(port).isInstanceOf(ProblemCommandPort.class);

    Class<?> implType =
        Class.forName("com.devouk.devouk_back.infra.problem.JpaProblemCommandRepository");
    assertThat(port).isInstanceOf(implType);
  }
}
