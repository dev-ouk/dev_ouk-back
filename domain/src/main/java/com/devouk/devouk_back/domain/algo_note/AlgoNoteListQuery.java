package com.devouk.devouk_back.domain.algo_note;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlgoNoteListQuery {

  private final String q;
  private final List<String> tagSlugs;

  private final OffsetDateTime createdAtFrom;
  private final OffsetDateTime createdAtTo;

  private final AlgoNoteListSortOption sort;
  private final int size;

  private final OffsetDateTime cursorCreatedAt;
  private final Long cursorNoteId;
}
