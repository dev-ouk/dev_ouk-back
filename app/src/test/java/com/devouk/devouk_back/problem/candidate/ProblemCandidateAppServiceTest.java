package com.devouk.devouk_back.problem.candidate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.devouk.devouk_back.domain.common.exception.InvalidProblemSiteException;
import com.devouk.devouk_back.domain.problem.*;
import com.devouk.devouk_back.problem.candidate.dto.ProblemCandidatesResponse;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ProblemCandidateAppServiceTest {

  @Test
  void search_buildsQueryFromParams_andMapsToResponse() {
    ProblemCandidateQueryPort port = mock(ProblemCandidateQueryPort.class);
    CursorCodec codec = mock(CursorCodec.class);
    ProblemCandidateAppService service = new ProblemCandidateAppService(port, codec);

    String q = "100";
    List<String> sites = List.of("baekjoon", "PROGRAMMERS");
    Integer size = 30;
    String cursor = "encoded-cursor";

    when(codec.decode("encoded-cursor")).thenReturn(10L);

    ProblemLastAttemptSummary summary =
        new ProblemLastAttemptSummary(
            AttemptVerdict.AC, OffsetDateTime.parse("2025-12-05T10:00:00Z"));

    ProblemCandidate candidate =
        new ProblemCandidate(ProblemSite.BAEKJOON, "1000", "A+B", 1, summary);

    ProblemCandidatePage page = new ProblemCandidatePage(List.of(candidate), size, false, null);

    when(port.search(any(ProblemCandidateQuery.class))).thenReturn(page);
    when(codec.encode(null)).thenReturn(null);

    ProblemCandidatesResponse response = service.search(q, sites, size, cursor);

    ArgumentCaptor<ProblemCandidateQuery> captor =
        ArgumentCaptor.forClass(ProblemCandidateQuery.class);
    verify(port).search(captor.capture());

    ProblemCandidateQuery sent = captor.getValue();
    assertThat(sent.getQ()).isEqualTo("100");
    assertThat(sent.getSites()).containsExactly(ProblemSite.BAEKJOON, ProblemSite.PROGRAMMERS);
    assertThat(sent.getSize()).isEqualTo(30);
    assertThat(sent.getCursorProblemId()).isEqualTo(10L);

    assertThat(response.getItems()).hasSize(1);
    ProblemCandidatesResponse.Item item = response.getItems().get(0);
    assertThat(item.getSite()).isEqualTo("BAEKJOON");
    assertThat(item.getSiteProblemId()).isEqualTo("1000");
    assertThat(item.getTitle()).isEqualTo("A+B");
    assertThat(item.getDifficulty()).isEqualTo(1);
    assertThat(item.getLastAttempt()).isNotNull();
    assertThat(item.getLastAttempt().getVerdict()).isEqualTo("AC");
    assertThat(item.getLastAttempt().getAttemptedAt()).isEqualTo("2025-12-05T10:00:00Z");

    assertThat(response.getSize()).isEqualTo(30);
    assertThat(response.isHasNext()).isFalse();
    assertThat(response.getNextCursor()).isNull();
  }

  @Test
  void search_siteListNullOrEmpty_resultsInEmptySitesInQuery() {
    ProblemCandidateQueryPort port = mock(ProblemCandidateQueryPort.class);
    CursorCodec codec = mock(CursorCodec.class);
    ProblemCandidateAppService service = new ProblemCandidateAppService(port, codec);

    when(codec.decode(null)).thenReturn(null);

    ProblemCandidatePage emptyPage =
        new ProblemCandidatePage(Collections.emptyList(), 20, false, null);
    when(port.search(any(ProblemCandidateQuery.class))).thenReturn(emptyPage);
    when(codec.encode(null)).thenReturn(null);

    service.search(null, null, null, null);

    ArgumentCaptor<ProblemCandidateQuery> captor =
        ArgumentCaptor.forClass(ProblemCandidateQuery.class);
    verify(port).search(captor.capture());

    ProblemCandidateQuery sent = captor.getValue();
    assertThat(sent.getSites()).isEmpty();
    assertThat(sent.getSize()).isEqualTo(20); // default size
  }

  @Test
  void search_sizeIsClampedBetween1And100() {
    ProblemCandidateQueryPort port = mock(ProblemCandidateQueryPort.class);
    CursorCodec codec = mock(CursorCodec.class);
    ProblemCandidateAppService service = new ProblemCandidateAppService(port, codec);

    when(codec.decode(any())).thenReturn(null);

    ProblemCandidatePage emptyPage1 =
        new ProblemCandidatePage(Collections.emptyList(), 1, false, null);
    when(port.search(any(ProblemCandidateQuery.class))).thenReturn(emptyPage1);
    when(codec.encode(null)).thenReturn(null);

    service.search(null, List.of(), 0, null);
    ArgumentCaptor<ProblemCandidateQuery> captor =
        ArgumentCaptor.forClass(ProblemCandidateQuery.class);
    verify(port).search(captor.capture());
    assertThat(captor.getValue().getSize()).isEqualTo(20); // size <=0 -> default 20

    reset(port);
    ProblemCandidatePage emptyPage2 =
        new ProblemCandidatePage(Collections.emptyList(), 100, false, null);
    when(port.search(any(ProblemCandidateQuery.class))).thenReturn(emptyPage2);

    service.search(null, List.of(), 1000, null);
    verify(port).search(captor.capture());
    assertThat(captor.getValue().getSize()).isEqualTo(100); // clamped to 100
  }

  @Test
  void search_invalidSite_throwsInvalidProblemSiteException() {
    ProblemCandidateQueryPort port = mock(ProblemCandidateQueryPort.class);
    CursorCodec codec = mock(CursorCodec.class);
    ProblemCandidateAppService service = new ProblemCandidateAppService(port, codec);

    assertThatThrownBy(() -> service.search(null, List.of("HACKERRANK"), 10, null))
        .isInstanceOf(InvalidProblemSiteException.class)
        .hasMessageContaining("HACKERRANK");

    verifyNoInteractions(port);
  }
}
