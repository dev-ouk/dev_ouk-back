package com.devouk.devouk_back.problem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import com.devouk.devouk_back.domain.problem.ProblemPreview;
import com.devouk.devouk_back.domain.problem.ProblemPreviewPort;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import com.devouk.devouk_back.problem.dto.ProblemPreviewResponse;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ProblemPreviewAppServiceTest {
  @Test
  void getPreview_delegatesToPort_andMapsToResponse() {
    ProblemPreviewPort port = mock(ProblemPreviewPort.class);
    ProblemPreviewAppService service = new ProblemPreviewAppService(port);

    String url = "https://www.acmicpc.net/problem/1000";

    ProblemPreview preview =
        new ProblemPreview(
            ProblemSite.BAEKJOON,
            "1000",
            "A+B",
            url,
            1,
            Map.of("algo", List.of("implementation", "arithmetic")));

    given(port.getPreview(url)).willReturn(preview);

    ProblemPreviewResponse response = service.getPreview(url);

    then(port).should().getPreview(url);

    assertThat(response.getSite()).isEqualTo("BAEKJOON");
    assertThat(response.getSiteProblemId()).isEqualTo("1000");
    assertThat(response.getTitle()).isEqualTo("A+B");
    assertThat(response.getUrl()).isEqualTo(url);
    assertThat(response.getDifficulty()).isEqualTo(1);
    assertThat(response.getTaxonomies())
        .containsKey("algo")
        .extractingByKey("algo")
        .satisfies(
            v -> {
              var t = (ProblemPreviewResponse.TaxonomySuggestionResponse) v;
              assertThat(t.getSuggestedTermSlugs())
                  .containsExactlyInAnyOrder("implementation", "arithmetic");
            });
  }
}
