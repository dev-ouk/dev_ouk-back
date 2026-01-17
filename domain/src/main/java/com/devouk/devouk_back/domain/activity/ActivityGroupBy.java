package com.devouk.devouk_back.domain.activity;

import java.util.Locale;

public enum ActivityGroupBy {
  DAY,
  WEEK,
  MONTH;

  public static ActivityGroupBy parseOrDefault(String raw) {
    if (raw == null || raw.isBlank()) {
      return DAY;
    }
    String v = raw.trim().toUpperCase(Locale.ROOT);
    return switch (v) {
      case "DAY" -> DAY;
      case "WEEK" -> WEEK;
      case "MONTH" -> MONTH;
      default -> throw new IllegalArgumentException("Unsupported groupBy=" + raw);
    };
  }
}
