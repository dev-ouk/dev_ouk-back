package com.devouk.devouk_back.taxonomy.service;

import com.devouk.devouk_back.domain.taxonomy.TaxonomyQueryPort;
import com.devouk.devouk_back.taxonomy.dto.TaxonomyTermsResponse;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaxonomyAppService {
  private final TaxonomyQueryPort taxonomyQueryPort;

  public TaxonomyAppService(TaxonomyQueryPort taxonomyQueryPort) {
    this.taxonomyQueryPort = taxonomyQueryPort;
  }

  public TaxonomyTermsResponse getTerms(String taxonomyCode, Optional<String> q) {
    return TaxonomyTermsResponse.from(taxonomyQueryPort.getTerms(taxonomyCode, q));
  }
}
