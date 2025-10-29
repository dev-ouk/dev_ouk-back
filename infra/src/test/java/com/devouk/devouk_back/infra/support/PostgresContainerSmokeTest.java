package com.devouk.devouk_back.infra.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class PostgresContainerSmokeTest {

  @Container
  static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:15-alpine")
          .withDatabaseName("testdb")
          .withUsername("testuser")
          .withPassword("testpass");

  @Test
  @DisplayName("PostgreSQL 컨테이너가 뜨고 쿼리를 실행할 수 있다.")
  void container_start_and_query_ok() throws Exception {
    try (Connection conn = postgres.createConnection("")) {
      try (Statement st = conn.createStatement()) {
        st.execute("CREATE TABLE demo(id INT PRIMARY KEY, name TEXT)");
        st.execute("INSERT INTO demo(id, name) VALUES (1, 'hello')");
        ResultSet rs = st.executeQuery("SELECT name FROM demo WHERE id = 1");

        assertThat(rs.next()).isTrue();
        assertThat(rs.getString("name")).isEqualTo("hello");
      }
    }
  }
}
