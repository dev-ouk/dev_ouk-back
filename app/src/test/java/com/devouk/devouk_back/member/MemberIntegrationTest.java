package com.devouk.devouk_back.member;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class MemberIntegrationTest {

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper objectMapper;

  @Test
  @DisplayName("POST /members 후 같은 멤버를 GET으로 조회할 수 있다 (통합 happy path)")
  void endToEnd_createAndGetMember_success() throws Exception {
    String body =
        """
            {
              "email": "integration@test.com",
              "name": "인티"
            }
            """;

    var postResult =
        mockMvc
            .perform(
                post("/members")
                    .with(csrf())
                    .with(user("testUser"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.email").value("integration@test.com"))
            .andExpect(jsonPath("$.name").value("인티"))
            .andReturn();

    String correlationIdHeader = postResult.getResponse().getHeader("X-Correlation-ID");
    assertThat(correlationIdHeader).isNotBlank();

    String responseJson = postResult.getResponse().getContentAsString();
    JsonNode node = objectMapper.readTree(responseJson);
    long createdId = node.get("id").asLong();

    mockMvc
        .perform(get("/members/{id}", createdId).with(csrf()).with(user("testUser")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(createdId))
        .andExpect(jsonPath("$.email").value("integration@test.com"))
        .andExpect(jsonPath("$.name").value("인티"));

    mockMvc
        .perform(get("/members").with(csrf()).with(user("testUser")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].email").value("integration@test.com"));
  }
}
