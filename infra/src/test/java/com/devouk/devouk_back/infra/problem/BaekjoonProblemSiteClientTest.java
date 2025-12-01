package com.devouk.devouk_back.infra.problem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.devouk.devouk_back.domain.common.exception.ProblemPreviewFetchException;
import com.devouk.devouk_back.domain.problem.ProblemPreview;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

class BaekjoonProblemSiteClientTest {

  @Test
  void supports_returnsTrueForBaekjoonDomain() throws Exception {
    RestClient.Builder builder = mock(RestClient.Builder.class, RETURNS_DEEP_STUBS);
    BaekjoonProblemSiteClient client = new BaekjoonProblemSiteClient(builder);

    assertThat(client.supports(new URI("https://www.acmicpc.net/problem/1000"))).isTrue();
    assertThat(client.supports(new URI("https://acmicpc.net/problem/1000"))).isTrue();
    assertThat(client.supports(new URI("https://www.google.com"))).isFalse();
  }

  @Test
  void fetchPreview_success_parsesResponse() throws Exception {
    // arrange RestClient chain
    RestClient.Builder builder = mock(RestClient.Builder.class);
    RestClient restClient = mock(RestClient.class);
    RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
    RestClient.RequestHeadersSpec headersSpec = (RestClient.RequestHeadersSpec<?>) uriSpec;
    RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

    when(builder.baseUrl(anyString())).thenReturn(builder);
    when(builder.build()).thenReturn(restClient);
    when(restClient.get()).thenReturn(uriSpec);

    when(uriSpec.uri(anyString(), any(Object[].class))).thenReturn(headersSpec);

    when(headersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);

    BaekjoonProblemSiteClient.SolvedAcTag tag1 = new BaekjoonProblemSiteClient.SolvedAcTag();
    // 리플렉션 없이 필드 접근이 안되면, 빌더 대신 실제 응답 객체 생성자 추가해도 됨
    // 여기서는 간단히 Mockito로도 가능
    BaekjoonProblemSiteClient.SolvedAcProblemResponse resp =
        mock(BaekjoonProblemSiteClient.SolvedAcProblemResponse.class);
    when(resp.getProblemId()).thenReturn(1000);
    when(resp.getTitleKo()).thenReturn("A+B");
    when(resp.getLevel()).thenReturn(1);
    when(resp.getTags()).thenReturn(List.of(tag1));

    when(responseSpec.body(any(Class.class))).thenReturn(resp);

    BaekjoonProblemSiteClient client = new BaekjoonProblemSiteClient(builder);

    ProblemPreview preview = client.fetchPreview(new URI("https://www.acmicpc.net/problem/1000"));

    assertThat(preview.getSiteProblemId()).isEqualTo("1000");
    assertThat(preview.getTitle()).isEqualTo("A+B");
    assertThat(preview.getDifficulty()).isEqualTo(1);
    assertThat(preview.getUrl()).isEqualTo("https://www.acmicpc.net/problem/1000");
    // tags는 mock이라 key가 null이지만, Map 자체가 만들어지는지 정도만 확인
    assertThat(preview.getSuggestedTaxonomyTermSlugs()).containsKey("algo");
  }

  @Test
  void fetchPreview_whenBodyNull_throwsProblemPreviewFetchException() throws Exception {
    RestClient.Builder builder = mock(RestClient.Builder.class);
    RestClient restClient = mock(RestClient.class);
    RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
    RestClient.RequestHeadersSpec headersSpec = (RestClient.RequestHeadersSpec<?>) uriSpec;
    RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

    when(builder.baseUrl(anyString())).thenReturn(builder);
    when(builder.build()).thenReturn(restClient);
    when(restClient.get()).thenReturn(uriSpec);
    when(uriSpec.uri(anyString(), any(Object[].class))).thenReturn(headersSpec);
    when(headersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    when(responseSpec.body(any(Class.class))).thenReturn(null);

    BaekjoonProblemSiteClient client = new BaekjoonProblemSiteClient(builder);

    assertThatThrownBy(() -> client.fetchPreview(new URI("https://www.acmicpc.net/problem/1000")))
        .isInstanceOf(ProblemPreviewFetchException.class)
        .hasMessageContaining("응답 파싱");
  }

  @Test
  void fetchPreview_invalidPath_throwsProblemPreviewFetchException() throws Exception {
    RestClient.Builder builder = mock(RestClient.Builder.class, RETURNS_DEEP_STUBS);
    BaekjoonProblemSiteClient client = new BaekjoonProblemSiteClient(builder);

    assertThatThrownBy(() -> client.fetchPreview(new URI("https://www.acmicpc.net/other/1000")))
        .isInstanceOf(ProblemPreviewFetchException.class)
        .hasMessageContaining("백준 URL 형식이 올바르지 않습니다.");
  }
}
