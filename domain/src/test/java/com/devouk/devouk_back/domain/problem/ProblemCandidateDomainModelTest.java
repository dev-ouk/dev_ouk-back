package com.devouk.devouk_back.domain.problem;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProblemCandidateDomainModelTest {

  @Test
  void attemptVerdict_enum_values_exist() {
    // enum이 잘 정의되어 있는지 + values()까지 한 번 태우기
    assertThat(AttemptVerdict.valueOf("AC")).isEqualTo(AttemptVerdict.AC);
    assertThat(List.of(AttemptVerdict.values())).contains(AttemptVerdict.GAVE_UP);
  }

  @Test
  void lastAttemptSummary_holdsValues() {
    OffsetDateTime now = OffsetDateTime.parse("2025-12-05T10:00:00Z");
    ProblemLastAttemptSummary summary = new ProblemLastAttemptSummary(AttemptVerdict.WA, now);

    assertThat(summary.getVerdict()).isEqualTo(AttemptVerdict.WA);
    assertThat(summary.getAttemptedAt()).isEqualTo(now);
  }

  @Test
  void problemCandidate_holdsValues() {
    ProblemLastAttemptSummary summary =
        new ProblemLastAttemptSummary(
            AttemptVerdict.AC, OffsetDateTime.parse("2025-12-05T10:00:00Z"));

    ProblemCandidate candidate =
        new ProblemCandidate(ProblemSite.BAEKJOON, "1000", "A+B", 1, summary);

    assertThat(candidate.getSite()).isEqualTo(ProblemSite.BAEKJOON);
    assertThat(candidate.getSiteProblemId()).isEqualTo("1000");
    assertThat(candidate.getTitle()).isEqualTo("A+B");
    assertThat(candidate.getDifficulty()).isEqualTo(1);
    assertThat(candidate.getLastAttempt()).isSameAs(summary);
  }

  @Test
  void problemCandidateQuery_holdsValues() {
    ProblemCandidateQuery query =
        new ProblemCandidateQuery("10", List.of(ProblemSite.BAEKJOON), 20, 5L);

    assertThat(query.getQ()).isEqualTo("10");
    assertThat(query.getSites()).containsExactly(ProblemSite.BAEKJOON);
    assertThat(query.getSize()).isEqualTo(20);
    assertThat(query.getCursorProblemId()).isEqualTo(5L);
  }

  @Test
  void problemCandidatePage_holdsValues() {
    ProblemCandidate candidate =
        new ProblemCandidate(ProblemSite.PROGRAMMERS, "42583", "다리 건너기", 2, null);

    ProblemCandidatePage page = new ProblemCandidatePage(List.of(candidate), 10, true, 123L);

    assertThat(page.getItems()).hasSize(1).first().isSameAs(candidate);
    assertThat(page.getSize()).isEqualTo(10);
    assertThat(page.isHasNext()).isTrue();
    assertThat(page.getNextCursorProblemId()).isEqualTo(123L);
  }
}
