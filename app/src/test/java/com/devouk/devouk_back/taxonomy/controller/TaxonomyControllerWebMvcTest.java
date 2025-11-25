package com.devouk.devouk_back.taxonomy.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.devouk.devouk_back.domain.common.exception.TaxonomyNotFoundException;
import com.devouk.devouk_back.taxonomy.dto.TaxonomyResponse;
import com.devouk.devouk_back.taxonomy.dto.TaxonomyTermsResponse;
import com.devouk.devouk_back.taxonomy.dto.TermItemResponse;
import com.devouk.devouk_back.taxonomy.service.TaxonomyAppService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TaxonomyController.class)
public class TaxonomyControllerWebMvcTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean TaxonomyAppService taxonomyAppService;

  @Test
  @DisplayName("GET /api/v1/taxonomies/{taxonomy}/terms - 성공 시 TaxonomyTermsResponse를 반환한다")
  void getTerms_success() throws Exception {
    TaxonomyResponse taxonomy = new TaxonomyResponse("algo", "알고리즘");
    List<TermItemResponse> items =
        List.of(
            new TermItemResponse("dijkstra", "Dijkstra"),
            new TermItemResponse("two-pointer", "Two Pointer"));
    TaxonomyTermsResponse response =
        new TaxonomyTermsResponse(taxonomy, items, items.size(), "dij");

    given(taxonomyAppService.getTerms(eq("algo"), eq(Optional.of("dij")))).willReturn(response);

    mockMvc
        .perform(
            get("/api/v1/taxonomies/{taxonomy}/terms", "algo")
                .with(user("testUser"))
                .param("q", "dij"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.taxonomy.code").value("algo"))
        .andExpect(jsonPath("$.taxonomy.name").value("알고리즘"))
        .andExpect(jsonPath("$.items.length()").value(2))
        .andExpect(jsonPath("$.items[0].slug").value("dijkstra"))
        .andExpect(jsonPath("$.count").value(2))
        .andExpect(jsonPath("$.q").value("dij"));
  }

  @Test
  @DisplayName("GET /api/v1/taxonomies/{taxonomy}/terms - taxonomy가 없으면 404를 반환한다")
  void getTerms_taxonomyNotFound_returns404() throws Exception {
    given(taxonomyAppService.getTerms(eq("missing"), any()))
        .willThrow(new TaxonomyNotFoundException("missing"));

    mockMvc
        .perform(get("/api/v1/taxonomies/{taxonomy}/terms", "missing").with(user("testUser")))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("요청한 분류를 찾을 수 없습니다. code: missing"))
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.correlationId").exists());
  }
}
