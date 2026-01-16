package com.devouk.devouk_back.attempt.dto;

import com.devouk.devouk_back.domain.problem.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttemptListResponse {

  private final long total;
  private List<Item> items;

  public static AttemptListResponse from(AttemptHistoryResult r) {
    return new AttemptListResponse(r.getTotal(), r.getItems().stream().map(Item::from).toList());
  }

  @Getter
  @AllArgsConstructor
  public static class Item {
    private final UUID attemptUuid;
    private final Integer timeSpent;
    private final String language;
    private final String notes;
    private final String verdict;
    private final String code;
    private final OffsetDateTime attemptedAt;
    private final String failType;
    private final String failDetail;
    private final String solution;
    private final OffsetDateTime nextReviewAt;

    public static Item from(AttemptHistoryItem a) {
      return new Item(
          a.getAttemptUuid(),
          a.getTimeSpent(),
          presentLanguage(a.getLanguage()),
          a.getNotes(),
          presentVerdict(a.getVerdict()),
          a.getCode(),
          a.getAttemptedAt(),
          presentFailType(a.getFailType()),
          a.getFailDetail(),
          a.getSolution(),
          a.getNextReviewAt());
    }

    private static String presentLanguage(AttemptLanguage lang) {
      if (lang == null) {
        return null;
      }
      return (lang == AttemptLanguage.CSHARP) ? "C#" : lang.name();
    }

    private static String presentVerdict(AttemptVerdict v) {
      return v != null ? v.name() : null;
    }

    private static String presentFailType(AttemptFailType t) {
      return t != null ? t.name() : null;
    }
  }
}
