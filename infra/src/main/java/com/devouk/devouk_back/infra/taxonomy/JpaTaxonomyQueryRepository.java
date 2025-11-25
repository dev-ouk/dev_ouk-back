package com.devouk.devouk_back.infra.taxonomy;

import com.devouk.devouk_back.domain.common.exception.TaxonomyNotFoundException;
import com.devouk.devouk_back.domain.taxonomy.Taxonomy;
import com.devouk.devouk_back.domain.taxonomy.TaxonomyQueryPort;
import com.devouk.devouk_back.domain.taxonomy.TaxonomyTerms;
import com.devouk.devouk_back.domain.taxonomy.Term;
import java.util.List;
import java.util.Optional;

public class JpaTaxonomyQueryRepository implements TaxonomyQueryPort {

  private final TermJpaRepository termRepo;
  private final TaxonomyJpaRepository taxonomyRepo;

  public JpaTaxonomyQueryRepository(
      TermJpaRepository termRepo, TaxonomyJpaRepository taxonomyRepo) {
    this.termRepo = termRepo;
    this.taxonomyRepo = taxonomyRepo;
  }

  @Override
  public TaxonomyTerms getTerms(String taxonomyCode, Optional<String> qOpt) {
    var tx =
        taxonomyRepo
            .findByCode(taxonomyCode)
            .orElseThrow(() -> new TaxonomyNotFoundException(taxonomyCode));

    var q = qOpt.map(s -> s.trim()).filter(s -> !s.isEmpty()).orElse(null);

    List<TermEntity> entities = termRepo.searchByTaxonomyAndQ(taxonomyCode, q);

    var taxonomy = new Taxonomy(tx.getCode(), tx.getName());
    var items = entities.stream().map(e -> new Term(e.getSlug(), e.getName())).toList();

    return new TaxonomyTerms(taxonomy, items, items.size(), q);
  }
}
