package com.devouk.devouk_back.problem.candidate;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devouk.devouk_back.problem.candidate.dto.ProblemCandidatesResponse;
import com.devouk.devouk_back.problem.candidate.dto.ProblemCandidatesResponse.Item;
import com.devouk.devouk_back.problem.candidate.dto.ProblemCandidatesResponse.LastAttempt;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProblemCandidateController.class)
class ProblemCandidateControllerWebMvcTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean ProblemCandidateAppService appService;

  @Test
  @DisplayName("GET /api/v1/problem-candidates - 성공 시 ProblemCandidatesResponse를 반환한다")
  void getProblemCandidates_success() throws Exception {
    LastAttempt lastAttempt = new LastAttempt("AC", "2025-12-05T10:00:00Z");
    Item item = new Item("BAEKJOON", "1000", "A+B", 1, lastAttempt);
    ProblemCandidatesResponse response =
        new ProblemCandidatesResponse(List.of(item), 20, false, null);

    BDDMockito.given(appService.search(anyString(), anyList(), any(), anyString()))
        .willReturn(response);

    mockMvc
        .perform(
            get("/api/v1/problem-candidates")
                .with(user("testUser"))
                .param("q", "100")
                .param("sites", "BAEKJOON")
                .param("size", "20")
                .param("cursor", "abc")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items.length()").value(1))
        .andExpect(jsonPath("$.items[0].site").value("BAEKJOON"))
        .andExpect(jsonPath("$.items[0].siteProblemId").value("1000"))
        .andExpect(jsonPath("$.items[0].title").value("A+B"))
        .andExpect(jsonPath("$.items[0].difficulty").value(1))
        .andExpect(jsonPath("$.items[0].lastAttempt.verdict").value("AC"))
        .andExpect(jsonPath("$.size").value(20))
        .andExpect(jsonPath("$.hasNext").value(false));
  }

  @Test
  @DisplayName("size가 범위를 벗어나면 400 Validation Failed를 반환한다")
  void getProblemCandidates_invalidSize_returns400() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/problem-candidates")
                .with(user("testUser"))
                .param("size", "0")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Validation Failed"))
        .andExpect(jsonPath("$.errors").isArray());
  }
}
