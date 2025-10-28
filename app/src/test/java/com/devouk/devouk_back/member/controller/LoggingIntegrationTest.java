package com.devouk.devouk_back.member.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import ch.qos.logback.classic.Logger;
import com.devouk.devouk_back.common.TestLogAppender;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class LoggingIntegrationTest {

  @Autowired MockMvc mockMvc;

  @Test
  void log_contains_correlationId() throws Exception {
    TestLogAppender appender = new TestLogAppender();
    appender.start();
    ((Logger) LoggerFactory.getLogger("ROOT")).addAppender(appender);

    var result =
        mockMvc
            .perform(
                post("/members")
                    .with(csrf())
                    .with(user("testUser"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                                        {
                                          "email": "logtest@test.com",
                                          "name": "로그테스트"
                                        }
                                        """))
            .andReturn();

    String cid = result.getResponse().getHeader("X-Correlation-ID");
    assertThat(cid).isNotBlank();

    assertThat(appender.eventsAsString()).anyMatch(line -> line.contains(cid));

    ((Logger) LoggerFactory.getLogger("ROOT")).detachAppender(appender);
  }
}
