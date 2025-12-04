package com.devouk.devouk_back.problem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devouk.devouk_back.domain.problem.Problem;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import com.devouk.devouk_back.problem.dto.ProblemCreateRequest;
import com.devouk.devouk_back.problem.service.ProblemAppService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProblemController.class)
class ProblemControllerWebMvcTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean ProblemAppService problemAppService;

  @Test
  @DisplayName("POST /api/v1/problems - 성공 시 ProblemResponse를 반환한다")
  void create_success() throws Exception {
    Problem domain =
        new Problem(
            1L,
            ProblemSite.BAEKJOON,
            "1000",
            "A+B",
            "https://www.acmicpc.net/problem/1000",
            1,
            List.of("implementation", "math"));

    org.mockito.BDDMockito.given(problemAppService.createProblem(any(ProblemCreateRequest.class)))
        .willReturn(domain);

    String body =
        """
                {
                  "site": "BAEKJOON",
                  "siteProblemId": "1000",
                  "title": "A+B",
                  "url": "https://www.acmicpc.net/problem/1000",
                  "difficulty": 1,
                  "tagSlugs": ["implementation", "math"]
                }
                """;

    mockMvc
        .perform(
            post("/api/v1/problems")
                .with(csrf())
                .with(user("testUser"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.problemId").value(1))
        .andExpect(jsonPath("$.site").value("BAEKJOON"))
        .andExpect(jsonPath("$.siteProblemId").value("1000"))
        .andExpect(jsonPath("$.title").value("A+B"))
        .andExpect(jsonPath("$.url").value("https://www.acmicpc.net/problem/1000"))
        .andExpect(jsonPath("$.difficulty").value(1))
        .andExpect(jsonPath("$.tagSlugs[0]").value("implementation"));
  }

  @Test
  @DisplayName("POST /api/v1/problems - 필수 필드가 비어 있으면 400 Validation Failed를 반환한다")
  void create_validationError_whenRequiredFieldBlank() throws Exception {
    String body =
        """
                {
                  "site": "",
                  "siteProblemId": "1000",
                  "title": "A+B",
                  "url": "https://www.acmicpc.net/problem/1000",
                  "difficulty": 1
                }
                """;

    mockMvc
        .perform(
            post("/api/v1/problems")
                .with(csrf())
                .with(user("testUser"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Validation Failed"))
        .andExpect(jsonPath("$.errors[0].field").value("site"));
  }
}
