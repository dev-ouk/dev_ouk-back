package com.devouk.devouk_back.infra.problem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.devouk.devouk_back.domain.common.exception.DuplicateProblemException;
import com.devouk.devouk_back.domain.common.exception.ProblemTagNotFoundException;
import com.devouk.devouk_back.domain.common.exception.TaxonomyNotFoundException;
import com.devouk.devouk_back.domain.problem.CreateProblemCommand;
import com.devouk.devouk_back.domain.problem.Problem;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import com.devouk.devouk_back.infra.taxonomy.TermEntity;
import com.devouk.devouk_back.infra.taxonomy.TermJpaRepository;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JpaProblemCommandRepositoryTest {

  private ProblemJpaRepository problemRepo;
  private TermJpaRepository termRepo;
  private JpaProblemCommandRepository repository;

  @BeforeEach
  void setUp() {
    problemRepo = mock(ProblemJpaRepository.class);
    termRepo = mock(TermJpaRepository.class);
    repository = new JpaProblemCommandRepository(problemRepo, termRepo);
  }

  @Test
  @DisplayName("tagSlugs가 null이면 taxonomy 조회 없이 문제를 저장한다")
  void create_withNullTags_savesProblemWithoutTerms() throws Exception {
    // given
    given(problemRepo.existsBySiteAndSiteProblemId(ProblemSite.BAEKJOON, "1000")).willReturn(false);

    given(problemRepo.save(any(ProblemEntity.class)))
        .willAnswer(
            invocation -> {
              ProblemEntity entity = invocation.getArgument(0);
              // id 세팅 (JPA 없이 테스트에서만)
              Field idField = ProblemEntity.class.getDeclaredField("id");
              idField.setAccessible(true);
              idField.set(entity, 1L);
              return entity;
            });

    CreateProblemCommand command =
        new CreateProblemCommand(
            ProblemSite.BAEKJOON,
            "1000",
            "A+B",
            "https://www.acmicpc.net/problem/1000",
            1,
            null // tagSlugs == null
            );

    // when
    Problem result = repository.create(command);

    // then
    verify(problemRepo).existsBySiteAndSiteProblemId(ProblemSite.BAEKJOON, "1000");
    verify(termRepo, never()).existsByTaxonomy_Code(anyString());
    verify(termRepo, never()).findByTaxonomy_CodeAndSlugIn(anyString(), anyCollection());

    assertThat(result.getId()).isEqualTo(1L);
    assertThat(result.getSite()).isEqualTo(ProblemSite.BAEKJOON);
    assertThat(result.getSiteProblemId()).isEqualTo("1000");
    assertThat(result.getTitle()).isEqualTo("A+B");
    assertThat(result.getUrl()).isEqualTo("https://www.acmicpc.net/problem/1000");
    assertThat(result.getDifficulty()).isEqualTo(1);
    assertThat(result.getTagSlugs()).isEmpty(); // null → 빈 리스트
  }

  @Test
  @DisplayName("tagSlugs가 있으면 taxonomy(algo)와 term들을 조회하고, 모두 존재하면 저장에 성공한다")
  void create_withTags_savesProblemWithTerms() throws Exception {
    // given
    given(problemRepo.existsBySiteAndSiteProblemId(ProblemSite.BAEKJOON, "1000")).willReturn(false);

    given(termRepo.existsByTaxonomy_Code("algo")).willReturn(true);

    TermEntity impl = mock(TermEntity.class);
    TermEntity math = mock(TermEntity.class);
    given(impl.getSlug()).willReturn("implementation");
    given(math.getSlug()).willReturn("math");

    given(termRepo.findByTaxonomy_CodeAndSlugIn("algo", List.of("implementation", "math")))
        .willReturn(List.of(impl, math));

    given(problemRepo.save(any(ProblemEntity.class)))
        .willAnswer(
            invocation -> {
              ProblemEntity entity = invocation.getArgument(0);
              Field idField = ProblemEntity.class.getDeclaredField("id");
              idField.setAccessible(true);
              idField.set(entity, 10L);
              return entity;
            });

    CreateProblemCommand command =
        new CreateProblemCommand(
            ProblemSite.BAEKJOON,
            "1000",
            "A+B",
            "https://www.acmicpc.net/problem/1000",
            1,
            List.of("implementation", "math"));

    // when
    Problem result = repository.create(command);

    // then
    verify(termRepo).existsByTaxonomy_Code("algo");
    verify(termRepo).findByTaxonomy_CodeAndSlugIn("algo", List.of("implementation", "math"));

    assertThat(result.getId()).isEqualTo(10L);
    assertThat(result.getTagSlugs()).containsExactlyInAnyOrder("implementation", "math");

    var entityCaptor = org.mockito.ArgumentCaptor.forClass(ProblemEntity.class);
    verify(problemRepo).save(entityCaptor.capture());
    ProblemEntity saved = entityCaptor.getValue();
    Set<TermEntity> terms = saved.getTerms();
    assertThat(terms).containsExactlyInAnyOrder(impl, math);
  }

  @Test
  @DisplayName("이미 같은 (site, siteProblemId)가 있으면 DuplicateProblemException을 던진다")
  void create_duplicate_throwsDuplicateProblemException() {
    // given
    given(problemRepo.existsBySiteAndSiteProblemId(ProblemSite.BAEKJOON, "1000")).willReturn(true);

    CreateProblemCommand command =
        new CreateProblemCommand(
            ProblemSite.BAEKJOON,
            "1000",
            "A+B",
            "https://www.acmicpc.net/problem/1000",
            1,
            List.of());

    // when & then
    assertThatThrownBy(() -> repository.create(command))
        .isInstanceOf(DuplicateProblemException.class)
        .hasMessageContaining("BAEKJOON")
        .hasMessageContaining("1000");

    verify(problemRepo, never()).save(any());
    verify(termRepo, never()).existsByTaxonomy_Code(anyString());
  }

  @Test
  @DisplayName("tagSlugs가 있는데 taxonomy(algo)가 없으면 TaxonomyNotFoundException을 던진다")
  void create_whenTaxonomyMissing_throwsTaxonomyNotFoundException() {
    // given
    given(problemRepo.existsBySiteAndSiteProblemId(ProblemSite.BAEKJOON, "1000")).willReturn(false);

    given(termRepo.existsByTaxonomy_Code("algo")).willReturn(false);

    CreateProblemCommand command =
        new CreateProblemCommand(
            ProblemSite.BAEKJOON,
            "1000",
            "A+B",
            "https://www.acmicpc.net/problem/1000",
            1,
            List.of("implementation"));

    // when & then
    assertThatThrownBy(() -> repository.create(command))
        .isInstanceOf(TaxonomyNotFoundException.class)
        .hasMessageContaining("algo");

    verify(termRepo, never()).findByTaxonomy_CodeAndSlugIn(anyString(), anyCollection());
  }

  @Test
  @DisplayName("요청한 tagSlugs 중 일부가 존재하지 않으면 ProblemTagNotFoundException을 던진다")
  void create_whenSomeTagsMissing_throwsProblemTagNotFoundException() {
    // given
    given(problemRepo.existsBySiteAndSiteProblemId(ProblemSite.BAEKJOON, "1000")).willReturn(false);

    given(termRepo.existsByTaxonomy_Code("algo")).willReturn(true);

    TermEntity impl = mock(TermEntity.class);
    given(impl.getSlug()).willReturn("implementation");

    // "math" 는 빠져 있음
    given(termRepo.findByTaxonomy_CodeAndSlugIn("algo", List.of("implementation", "math")))
        .willReturn(List.of(impl));

    CreateProblemCommand command =
        new CreateProblemCommand(
            ProblemSite.BAEKJOON,
            "1000",
            "A+B",
            "https://www.acmicpc.net/problem/1000",
            1,
            List.of("implementation", "math"));

    // when & then
    assertThatThrownBy(() -> repository.create(command))
        .isInstanceOf(ProblemTagNotFoundException.class)
        .hasMessageContaining("math");
  }
}
