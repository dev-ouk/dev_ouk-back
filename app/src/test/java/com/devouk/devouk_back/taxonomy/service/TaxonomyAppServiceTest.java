package com.devouk.devouk_back.taxonomy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.devouk.devouk_back.domain.taxonomy.Taxonomy;
import com.devouk.devouk_back.domain.taxonomy.TaxonomyQueryPort;
import com.devouk.devouk_back.domain.taxonomy.TaxonomyTerms;
import com.devouk.devouk_back.domain.taxonomy.Term;
import com.devouk.devouk_back.taxonomy.dto.TaxonomyTermsResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class TaxonomyAppServiceTest {

  @Test
  void getTerms_delegatesToPort_andMapsToResponse() {
    TaxonomyQueryPort port = mock(TaxonomyQueryPort.class);
    TaxonomyAppService service = new TaxonomyAppService(port);

    String taxonomyCode = "algo";
    String q = "dij";

    Taxonomy taxonomy = new Taxonomy("algo", "알고리즘");
    List<Term> terms =
        List.of(new Term("dijkstra", "Dijkstra"), new Term("two-pointer", "Two Pointer"));

    TaxonomyTerms domainResult = new TaxonomyTerms(taxonomy, terms, terms.size(), q);

    given(port.getTerms(taxonomyCode, Optional.of(q))).willReturn(domainResult);

    TaxonomyTermsResponse response = service.getTerms(taxonomyCode, Optional.of(q));

    verify(port).getTerms(taxonomyCode, Optional.of(q));

    assertThat(response.getTaxonomy().getCode()).isEqualTo("algo");
    assertThat(response.getTaxonomy().getName()).isEqualTo("알고리즘");
    assertThat(response.getItems()).hasSize(2);
    assertThat(response.getItems().get(0).getSlug()).isEqualTo("dijkstra");
    assertThat(response.getCount()).isEqualTo(2);
    assertThat(response.getQ()).isEqualTo(q);
  }
}
