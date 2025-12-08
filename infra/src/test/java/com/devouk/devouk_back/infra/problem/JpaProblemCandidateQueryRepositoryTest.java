package com.devouk.devouk_back.infra.problem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.devouk.devouk_back.domain.problem.*;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Pageable;

class JpaProblemCandidateQueryRepositoryTest {

  private ProblemJpaRepository problemRepo;
  private AttemptJpaRepository attemptRepo;
  private JpaProblemCandidateQueryRepository repository;

  @BeforeEach
  void setUp() {
    problemRepo = mock(ProblemJpaRepository.class);
    attemptRepo = mock(AttemptJpaRepository.class);
    repository = new JpaProblemCandidateQueryRepository(problemRepo, attemptRepo);
  }

  @Test
  @DisplayName("sites가 비어 있고 결과가 없으면 빈 페이지를 반환하고 attempt 조회를 하지 않는다")
  void search_noSites_noResults_returnsEmptyPage() {
    ProblemCandidateQuery query = new ProblemCandidateQuery(null, List.of(), 20, null);

    given(problemRepo.searchCandidatesNoSite(isNull(), isNull(), any(Pageable.class)))
        .willReturn(List.of());

    ProblemCandidatePage page = repository.search(query);

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(problemRepo).searchCandidatesNoSite(isNull(), isNull(), pageableCaptor.capture());

    Pageable pageable = pageableCaptor.getValue();
    assertThat(pageable.getPageSize()).isEqualTo(21); // size + 1

    verifyNoInteractions(attemptRepo);

    assertThat(page.getItems()).isEmpty();
    assertThat(page.getSize()).isEqualTo(20);
    assertThat(page.isHasNext()).isFalse();
    assertThat(page.getNextCursorProblemId()).isNull();
  }

  @Test
  @DisplayName("sites가 지정되면 searchCandidatesWithSites를 사용하고 hasNext와 nextCursor를 계산한다")
  void search_withSites_usesSearchWithSites_andComputesHasNext() {
    // given
    ProblemCandidateQuery query =
        new ProblemCandidateQuery(" 10 ", List.of(ProblemSite.BAEKJOON), 2, 5L);

    ProblemEntity p1 = mockProblem(6L, ProblemSite.BAEKJOON, "1000", "A+B", 1);
    ProblemEntity p2 = mockProblem(7L, ProblemSite.BAEKJOON, "1001", "A-B", 2);
    ProblemEntity p3 = mockProblem(8L, ProblemSite.BAEKJOON, "1002", "A*B", 3);

    given(
            problemRepo.searchCandidatesWithSites(
                anyString(), anyList(), anyLong(), any(Pageable.class)))
        .willReturn(List.of(p1, p2, p3)); // pageSize(2) + 1 로 hasNext 판단

    OffsetDateTime older = OffsetDateTime.parse("2025-12-05T10:00:00Z");
    OffsetDateTime newer = OffsetDateTime.parse("2025-12-05T11:00:00Z");
    OffsetDateTime t2 = OffsetDateTime.parse("2025-12-06T09:00:00Z");

    AttemptEntity a1Older = mockAttempt(p1, older, AttemptVerdict.WA);
    AttemptEntity a1Newer = mockAttempt(p1, newer, AttemptVerdict.AC);
    AttemptEntity a2 = mockAttempt(p2, t2, AttemptVerdict.TLE);

    given(attemptRepo.findLatestByProblemIds(List.of(6L, 7L)))
        .willReturn(List.of(a1Older, a1Newer, a2));

    // when
    ProblemCandidatePage page = repository.search(query);

    // then - repo 호출 검증 (q normalize, cursor, size+1)
    assertSearchWithSitesCalled("10", 5L, 3);

    assertThat(page.isHasNext()).isTrue();
    assertThat(page.getSize()).isEqualTo(2);
    assertThat(page.getItems()).hasSize(2);
    assertThat(page.getNextCursorProblemId()).isEqualTo(7L);

    ProblemCandidate first = page.getItems().get(0);
    ProblemCandidate second = page.getItems().get(1);

    assertThat(first.getSiteProblemId()).isEqualTo("1000");
    assertThat(first.getLastAttempt()).isNotNull();
    assertThat(first.getLastAttempt().getVerdict()).isEqualTo(AttemptVerdict.AC);
    assertThat(first.getLastAttempt().getAttemptedAt()).isEqualTo(newer);

    assertThat(second.getSiteProblemId()).isEqualTo("1001");
    assertThat(second.getLastAttempt()).isNotNull();
    assertThat(second.getLastAttempt().getVerdict()).isEqualTo(AttemptVerdict.TLE);
    assertThat(second.getLastAttempt().getAttemptedAt()).isEqualTo(t2);
  }

  @Test
  @DisplayName("문제는 있지만 시도가 하나도 없으면 lastAttempt는 null이다")
  void search_whenNoAttempts_lastAttemptIsNull() {
    ProblemCandidateQuery query = new ProblemCandidateQuery(null, List.of(), 5, null);

    ProblemEntity p1 = mockProblem(1L, ProblemSite.PROGRAMMERS, "42583", "다리 건너기", 2);

    given(problemRepo.searchCandidatesNoSite(isNull(), isNull(), any(Pageable.class)))
        .willReturn(List.of(p1));

    given(attemptRepo.findLatestByProblemIds(List.of(1L))).willReturn(List.of());

    ProblemCandidatePage page = repository.search(query);

    assertThat(page.getItems()).hasSize(1);
    ProblemCandidate c = page.getItems().get(0);
    assertThat(c.getSite()).isEqualTo(ProblemSite.PROGRAMMERS);
    assertThat(c.getLastAttempt()).isNull();
  }

  // ====== 헬퍼 메서드들 ======

  private ProblemEntity mockProblem(
      Long id, ProblemSite site, String siteProblemId, String title, int difficulty) {

    ProblemEntity p = mock(ProblemEntity.class);
    given(p.getId()).willReturn(id);
    given(p.getSite()).willReturn(site);
    given(p.getSiteProblemId()).willReturn(siteProblemId);
    given(p.getTitle()).willReturn(title);
    given(p.getDifficulty()).willReturn(difficulty);
    return p;
  }

  private AttemptEntity mockAttempt(
      ProblemEntity problem, OffsetDateTime attemptedAt, AttemptVerdict verdict) {

    AttemptEntity a = mock(AttemptEntity.class);
    given(a.getProblem()).willReturn(problem);
    given(a.getAttemptedAt()).willReturn(attemptedAt);
    given(a.getVerdict()).willReturn(verdict);
    return a;
  }

  private void assertSearchWithSitesCalled(
      String expectedQ, Long expectedCursor, int expectedSize) {
    ArgumentCaptor<String> qCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Long> cursorCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

    verify(problemRepo)
        .searchCandidatesWithSites(
            qCaptor.capture(), anyList(), cursorCaptor.capture(), pageableCaptor.capture());

    assertThat(qCaptor.getValue()).isEqualTo(expectedQ); // normalizeQ 적용
    assertThat(cursorCaptor.getValue()).isEqualTo(expectedCursor);
    assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(expectedSize);
  }
}
