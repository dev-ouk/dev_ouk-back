package com.devouk.devouk_back.domain.activity;

import java.util.Locale;

public enum ActivityType {
  CODING_TEST;

  public static ActivityType parse(String raw) {
    if (raw == null || raw.isBlank()) {
      return null;
    }
    return ActivityType.valueOf(raw.trim().toUpperCase(Locale.ROOT));
  }
}
