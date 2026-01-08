package com.devouk.devouk_back.domain.problem;

import java.util.Locale;

public enum AttemptFailType {
  IMPLEMENTATION,
  ALGORITHM,
  EDGE_CASE,
  PERFORMANCE,
  CARELESS,
  OTHER;

  public static AttemptFailType parseNullable(String raw) {
    if (raw == null || raw.isBlank()) {
      return null;
    }
    return AttemptFailType.valueOf(raw.trim().toUpperCase(Locale.ROOT));
  }
}
