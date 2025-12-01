package com.devouk.devouk_back.infra.problem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.devouk.devouk_back.domain.common.exception.ProblemPreviewFetchException;
import com.devouk.devouk_back.domain.common.exception.UnsupportedProblemSiteException;
import com.devouk.devouk_back.domain.problem.ProblemPreview;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CompositeProblemPreviewAdapterTest {

  static class StubClient implements ProblemSiteClient {
    private final String hostFragment;
    private final String siteProblemId;

    StubClient(String hostFragment, String siteProblemId) {
      this.hostFragment = hostFragment;
      this.siteProblemId = siteProblemId;
    }

    @Override
    public boolean supports(URI uri) {
      return uri.getHost() != null && uri.getHost().contains(hostFragment);
    }

    @Override
    public ProblemPreview fetchPreview(URI uri) {
      return new ProblemPreview(
          ProblemSite.BAEKJOON,
          siteProblemId,
          "dummy",
          uri.toString(),
          3,
          Map.of("algo", List.of("impl")));
    }
  }

  @Test
  void getPreview_usesFirstSupportingClient() {
    ProblemSiteClient c1 = new StubClient("acmicpc.net", "1000");
    ProblemSiteClient c2 = new StubClient("programmers.co.kr", "42583");

    CompositeProblemPreviewAdapter adapter = new CompositeProblemPreviewAdapter(List.of(c1, c2));

    ProblemPreview preview = adapter.getPreview("https://www.acmicpc.net/problem/1000");

    assertThat(preview.getSiteProblemId()).isEqualTo("1000");
    assertThat(preview.getTitle()).isEqualTo("dummy");
  }

  @Test
  void getPreview_invalidUrl_throwsProblemPreviewFetchException() {
    CompositeProblemPreviewAdapter adapter = new CompositeProblemPreviewAdapter(List.of());

    assertThatThrownBy(() -> adapter.getPreview("ht tp://bad-url"))
        .isInstanceOf(ProblemPreviewFetchException.class)
        .hasMessageContaining("잘못된 URL 형식");
  }

  @Test
  void getPreview_unsupportedHost_throwsUnsupportedProblemSiteException() {
    CompositeProblemPreviewAdapter adapter =
        new CompositeProblemPreviewAdapter(List.of(new StubClient("acmicpc.net", "1000")));

    assertThatThrownBy(() -> adapter.getPreview("https://example.com/problem/1"))
        .isInstanceOf(UnsupportedProblemSiteException.class)
        .hasMessageContaining("example.com");
  }
}
