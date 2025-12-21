package com.devouk.devouk_back.domain.algo_note;

import com.devouk.devouk_back.domain.common.exception.InvalidAlgoNoteTitleException;
import java.text.Normalizer;
import java.util.Locale;

public class SimpleSlugifier implements Slugifier {

  private static final int MAX_SLUG_LENGTH = 200;

  @Override
  public String slugify(String title) {
    if (title == null || title.isBlank()) {
      throw new InvalidAlgoNoteTitleException("title은 비울 수 없습니다.");
    }

    String normalized =
        Normalizer.normalize(title, Normalizer.Form.NFKC).trim().toLowerCase(Locale.ROOT);

    String slug =
        normalized
            .replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}]+", "-")
            .replaceAll("-{2,}", "-")
            .replaceAll("^-+", "")
            .replaceAll("-+$", "");

    if (slug.isBlank()) {
      throw new InvalidAlgoNoteTitleException("slug를 생성할 수 없는 title 입니다.");
    }

    if (slug.length() > MAX_SLUG_LENGTH) {
      slug = slug.substring(0, MAX_SLUG_LENGTH);
      slug = slug.replaceAll("-+$", "");
      if (slug.isBlank()) {
        throw new InvalidAlgoNoteTitleException("slug 길이 제한 처리 후 값이 비었습니다.");
      }
    }

    return slug;
  }
}
