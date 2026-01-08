package com.devouk.devouk_back.domain.problem;

import java.util.Locale;

public enum AttemptLanguage {
  JAVA,
  PYTHON,
  CPP,
  KOTLIN,
  JS,
  C,
  CSHARP;

  public static AttemptLanguage parse(String raw) {
    if (raw == null || raw.isBlank()) {
      return null;
    }

    String v = raw.trim().toUpperCase(Locale.ROOT);

    if (v.equals("C#") || v.equals("C-SHARP") || v.equals("C_SHARP")) {
      return CSHARP;
    }

    return AttemptLanguage.valueOf(v);
  }
}
