package com.devouk.devouk_back.domain.taxonomy;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaxonomyTerms {
  private final Taxonomy taxonomy;
  private final List<Term> items;
  private final int count;
  private final String q;
}
