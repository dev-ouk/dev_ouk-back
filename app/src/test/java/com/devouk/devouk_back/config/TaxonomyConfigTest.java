package com.devouk.devouk_back.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class TaxonomyConfigTest {

  @Test
  void taxonomyQueryPort_returnsJpaImplementation() throws Exception {
    TaxonomyConfig config = new TaxonomyConfig();

    Method method = null;
    for (Method m : TaxonomyConfig.class.getDeclaredMethods()) {
      if (m.getName().equals("taxonomyQueryPort")) {
        method = m;
        break;
      }
    }
    assertThat(method).isNotNull();

    Class<?>[] paramTypes = method.getParameterTypes();
    assertThat(paramTypes).hasSize(2);

    Object[] args = new Object[] {null, null};

    Object port = method.invoke(config, args);

    Class<?> implType =
        Class.forName("com.devouk.devouk_back.infra.taxonomy.JpaTaxonomyQueryRepository");
    assertThat(port).isInstanceOf(implType);
  }
}
