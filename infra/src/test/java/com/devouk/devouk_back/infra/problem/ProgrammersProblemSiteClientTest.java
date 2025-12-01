package com.devouk.devouk_back.infra.problem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.devouk.devouk_back.domain.common.exception.ProblemPreviewFetchException;
import com.devouk.devouk_back.domain.problem.ProblemPreview;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

class ProgrammersProblemSiteClientTest {

  @Test
  void supports_checksProgrammersDomain() throws Exception {
    RestClient.Builder builder = mock(RestClient.Builder.class, RETURNS_DEEP_STUBS);
    ProgrammersProblemSiteClient client = new ProgrammersProblemSiteClient(builder);

    assertThat(
            client.supports(
                new URI("https://school.programmers.co.kr/learn/courses/30/lessons/42583")))
        .isTrue();
    assertThat(client.supports(new URI("https://programmers.co.kr/learn/courses/30/lessons/42583")))
        .isTrue();
    assertThat(client.supports(new URI("https://www.google.com"))).isFalse();
  }

  @Test
  void fetchPreview_success_parsesHtml() throws Exception {
    RestClient.Builder builder = mock(RestClient.Builder.class);
    RestClient restClient = mock(RestClient.class);
    RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
    RestClient.RequestHeadersSpec headersSpec = (RestClient.RequestHeadersSpec<?>) uriSpec;
    RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

    when(builder.build()).thenReturn(restClient);
    when(restClient.get()).thenReturn(uriSpec);
    when(uriSpec.uri(anyString())).thenReturn(headersSpec);
    when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
    when(headersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.body(String.class))
        .thenReturn(
            """
                        <html>
                          <head>
                            <meta property="og:title" content="코딩테스트 연습 - 다리 건너기">
                          </head>
                          <body>
                            <div class="lesson-content" data-lesson-title="다리 건너기" data-challenge-level="2"></div>
                          </body>
                        </html>
                        """);

    ProgrammersProblemSiteClient client = new ProgrammersProblemSiteClient(builder);

    ProblemPreview preview =
        client.fetchPreview(
            new URI("https://school.programmers.co.kr/learn/courses/30/lessons/42583"));

    assertThat(preview.getSiteProblemId()).isEqualTo("42583");
    assertThat(preview.getTitle()).contains("다리 건너기");
    assertThat(preview.getDifficulty()).isEqualTo(2);
    assertThat(preview.getSuggestedTaxonomyTermSlugs()).isEmpty();
  }

  @Test
  void fetchPreview_htmlNull_throwsProblemPreviewFetchException() throws Exception {
    RestClient.Builder builder = mock(RestClient.Builder.class);
    RestClient restClient = mock(RestClient.class);
    RestClient.RequestHeadersUriSpec uriSpec = mock(RestClient.RequestHeadersUriSpec.class);
    RestClient.RequestHeadersSpec headersSpec = (RestClient.RequestHeadersSpec<?>) uriSpec;
    RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

    when(builder.build()).thenReturn(restClient);
    when(restClient.get()).thenReturn(uriSpec);
    when(uriSpec.uri(anyString())).thenReturn(headersSpec);
    when(headersSpec.header(anyString(), anyString())).thenReturn(headersSpec);
    when(headersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.body(String.class)).thenReturn(null);

    ProgrammersProblemSiteClient client = new ProgrammersProblemSiteClient(builder);

    assertThatThrownBy(
            () ->
                client.fetchPreview(
                    new URI("https://school.programmers.co.kr/learn/courses/30/lessons/42583")))
        .isInstanceOf(ProblemPreviewFetchException.class)
        .hasMessageContaining("응답이 비어 있습니다");
  }

  @Test
  void fetchPreview_invalidPath_throwsProblemPreviewFetchException() throws Exception {
    RestClient.Builder builder = mock(RestClient.Builder.class, RETURNS_DEEP_STUBS);
    ProgrammersProblemSiteClient client = new ProgrammersProblemSiteClient(builder);

    assertThatThrownBy(
            () ->
                client.fetchPreview(
                    new URI("https://school.programmers.co.kr/learn/courses/30/other/42583")))
        .isInstanceOf(ProblemPreviewFetchException.class)
        .hasMessageContaining("lessons/{id} 형태의 URL이 아닙니다.");
  }
}
