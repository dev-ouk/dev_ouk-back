package com.devouk.devouk_back.domain.taxonomy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Term {
  private final String slug;
  private final String name;
}
