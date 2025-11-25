package com.devouk.devouk_back.taxonomy.dto;

import com.devouk.devouk_back.domain.taxonomy.Term;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TermItemResponse {
  private String slug;
  private String name;

  public static TermItemResponse from(Term t) {
    return new TermItemResponse(t.getSlug(), t.getName());
  }
}
