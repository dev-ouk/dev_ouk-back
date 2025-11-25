package com.devouk.devouk_back.taxonomy.dto;

import com.devouk.devouk_back.domain.taxonomy.Taxonomy;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaxonomyResponse {
  private String code;
  private String name;

  public static TaxonomyResponse from(Taxonomy t) {
    return new TaxonomyResponse(t.getCode(), t.getName());
  }
}
