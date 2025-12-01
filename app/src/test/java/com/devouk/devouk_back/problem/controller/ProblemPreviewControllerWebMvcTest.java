package com.devouk.devouk_back.problem.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devouk.devouk_back.domain.problem.ProblemPreview;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import com.devouk.devouk_back.problem.dto.ProblemPreviewResponse;
import com.devouk.devouk_back.problem.service.ProblemPreviewAppService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProblemPreviewController.class)
class ProblemPreviewControllerWebMvcTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean ProblemPreviewAppService appService;

  @Test
  @DisplayName("POST /api/v1/problem-previews - 성공 시 미리보기 정보를 반환한다")
  void preview_success() throws Exception {
    String url = "https://www.acmicpc.net/problem/1000";

    ProblemPreview preview =
        new ProblemPreview(
            ProblemSite.BAEKJOON,
            "1000",
            "A+B",
            url,
            1,
            Map.of("algo", List.of("implementation", "math")));

    given(appService.getPreview(anyString())).willReturn(ProblemPreviewResponse.from(preview));

    mockMvc
        .perform(
            post("/api/v1/problem-previews")
                .with(csrf())
                .with(user("testUser"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"" + url + "\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.site").value("BAEKJOON"))
        .andExpect(jsonPath("$.siteProblemId").value("1000"))
        .andExpect(jsonPath("$.title").value("A+B"))
        .andExpect(jsonPath("$.difficulty").value(1))
        .andExpect(jsonPath("$.taxonomies.algo.suggestedTermSlugs[0]").value("implementation"));
  }

  @Test
  @DisplayName("POST /api/v1/problem-previews - url이 비어있으면 400 Validation Failed를 반환한다")
  void preview_validationError() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/problem-previews")
                .with(csrf())
                .with(user("testUser"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\"\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Validation Failed"))
        .andExpect(jsonPath("$.errors[0].field").value("url"));
  }
}
