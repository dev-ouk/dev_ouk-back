package com.devouk.devouk_back.taxonomy.dto;

import com.devouk.devouk_back.domain.taxonomy.TaxonomyTerms;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaxonomyTermsResponse {
  private TaxonomyResponse taxonomy;
  private List<TermItemResponse> items;
  private int count;
  private String q;

  public static TaxonomyTermsResponse from(TaxonomyTerms tt) {
    List<TermItemResponse> mapped = tt.getItems().stream().map(TermItemResponse::from).toList();
    return new TaxonomyTermsResponse(
        TaxonomyResponse.from(tt.getTaxonomy()), mapped, tt.getCount(), tt.getQ());
  }
}
