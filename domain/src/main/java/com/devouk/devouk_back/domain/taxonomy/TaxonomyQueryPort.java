package com.devouk.devouk_back.domain.taxonomy;

import java.util.Optional;

public interface TaxonomyQueryPort {
  TaxonomyTerms getTerms(String taxonomyCode, Optional<String> q);
}
