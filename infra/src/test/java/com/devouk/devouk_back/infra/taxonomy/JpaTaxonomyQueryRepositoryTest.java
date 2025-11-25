package com.devouk.devouk_back.infra.taxonomy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.devouk.devouk_back.domain.common.exception.TaxonomyNotFoundException;
import com.devouk.devouk_back.domain.taxonomy.TaxonomyTerms;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JpaTaxonomyQueryRepositoryTest {

  private TermJpaRepository termRepo;
  private TaxonomyJpaRepository taxonomyRepo;
  private JpaTaxonomyQueryRepository repository;

  @BeforeEach
  void setUp() {
    termRepo = mock(TermJpaRepository.class);
    taxonomyRepo = mock(TaxonomyJpaRepository.class);
    repository = new JpaTaxonomyQueryRepository(termRepo, taxonomyRepo);
  }

  @Test
  @DisplayName("taxonomy와 q가 없으면 전체 term 목록과 null q를 반환한다")
  void getTerms_withoutQ_returnsAll() {
    // given
    TaxonomyEntity taxonomyEntity = mock(TaxonomyEntity.class);
    given(taxonomyEntity.getCode()).willReturn("algo");
    given(taxonomyEntity.getName()).willReturn("알고리즘");

    given(taxonomyRepo.findByCode("algo")).willReturn(Optional.of(taxonomyEntity));

    TermEntity t1 = mock(TermEntity.class);
    given(t1.getSlug()).willReturn("dijkstra");
    given(t1.getName()).willReturn("Dijkstra");

    TermEntity t2 = mock(TermEntity.class);
    given(t2.getSlug()).willReturn("two-pointer");
    given(t2.getName()).willReturn("Two Pointer");

    given(termRepo.searchByTaxonomyAndQ("algo", null)).willReturn(List.of(t1, t2));

    TaxonomyTerms result = repository.getTerms("algo", Optional.empty());

    verify(taxonomyRepo).findByCode("algo");
    verify(termRepo).searchByTaxonomyAndQ("algo", null);

    assertThat(result.getTaxonomy().getCode()).isEqualTo("algo");
    assertThat(result.getTaxonomy().getName()).isEqualTo("알고리즘");
    assertThat(result.getItems()).hasSize(2);
    assertThat(result.getItems().get(0).getSlug()).isEqualTo("dijkstra");
    assertThat(result.getCount()).isEqualTo(2);
    assertThat(result.getQ()).isNull(); // Optional.empty() → q == null
  }

  @Test
  @DisplayName("q가 공백만 있는 경우 trim 후 null로 처리된다")
  void getTerms_withBlankQ_treatedAsNull() {
    TaxonomyEntity taxonomyEntity = mock(TaxonomyEntity.class);
    given(taxonomyEntity.getCode()).willReturn("algo");
    given(taxonomyEntity.getName()).willReturn("알고리즘");

    given(taxonomyRepo.findByCode("algo")).willReturn(Optional.of(taxonomyEntity));

    TermEntity t1 = mock(TermEntity.class);
    given(t1.getSlug()).willReturn("bitmask");
    given(t1.getName()).willReturn("Bitmask");

    given(termRepo.searchByTaxonomyAndQ("algo", null)).willReturn(List.of(t1));

    TaxonomyTerms result = repository.getTerms("algo", Optional.of("   "));

    // then
    assertThat(result.getItems()).hasSize(1);
    assertThat(result.getItems().get(0).getSlug()).isEqualTo("bitmask");
    assertThat(result.getQ()).isNull();
  }

  @Test
  @DisplayName("q가 주어지면 그대로 repository에 전달되어 필터링된 결과를 반환한다")
  void getTerms_withQ_passedThrough() {
    TaxonomyEntity taxonomyEntity = mock(TaxonomyEntity.class);
    given(taxonomyEntity.getCode()).willReturn("algo");
    given(taxonomyEntity.getName()).willReturn("알고리즘");

    given(taxonomyRepo.findByCode("algo")).willReturn(Optional.of(taxonomyEntity));

    TermEntity t1 = mock(TermEntity.class);
    given(t1.getSlug()).willReturn("dijkstra");
    given(t1.getName()).willReturn("Dijkstra");

    given(termRepo.searchByTaxonomyAndQ("algo", "dij")).willReturn(List.of(t1));

    TaxonomyTerms result = repository.getTerms("algo", Optional.of("dij"));

    verify(termRepo).searchByTaxonomyAndQ("algo", "dij");

    assertThat(result.getItems()).hasSize(1);
    assertThat(result.getItems().get(0).getSlug()).isEqualTo("dijkstra");
    assertThat(result.getQ()).isEqualTo("dij");
  }

  @Test
  @DisplayName("taxonomy가 없으면 TaxonomyNotFoundException을 던진다")
  void getTerms_missingTaxonomy_throwsException() {
    given(taxonomyRepo.findByCode("missing")).willReturn(Optional.empty());

    assertThatThrownBy(() -> repository.getTerms("missing", Optional.empty()))
        .isInstanceOf(TaxonomyNotFoundException.class)
        .hasMessageContaining("missing");
  }
}
