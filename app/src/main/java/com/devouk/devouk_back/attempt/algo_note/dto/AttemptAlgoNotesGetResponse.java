package com.devouk.devouk_back.attempt.algo_note.dto;

import com.devouk.devouk_back.domain.problem.AttemptAlgoNoteItem;
import com.devouk.devouk_back.domain.problem.AttemptAlgoNotesResult;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttemptAlgoNotesGetResponse {

  private final long total;
  private final List<Item> items;

  public static AttemptAlgoNotesGetResponse from(AttemptAlgoNotesResult r) {
    List<Item> mapped = r.getItems().stream().map(Item::from).toList();
    return new AttemptAlgoNotesGetResponse(r.getTotal(), mapped);
  }

  @Getter
  @AllArgsConstructor
  public static class Item {
    private final String slug;
    private final String title;
    private final String createdAt;

    public static Item from(AttemptAlgoNoteItem i) {
      return new Item(i.getSlug(), i.getTitle(), format(i.getCreatedAt()));
    }

    private static String format(OffsetDateTime dt) {
      return dt == null ? null : dt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
  }
}
