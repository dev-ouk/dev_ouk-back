package com.devouk.devouk_back.domain.algo_note;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlgoNoteListPage {
  private final List<AlgoNoteListItem> items;
  private final int size;
  private final boolean hasNext;

  private final OffsetDateTime nextCursorCreatedAt;
  private final Long nextCursorNoteId;
}
