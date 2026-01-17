package com.devouk.devouk_back.domain.problem;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttemptAlgoNotesResult {
  private final long total;
  private final List<AttemptAlgoNoteItem> items;

  public static AttemptAlgoNotesResult of(List<AttemptAlgoNoteItem> items) {
    List<AttemptAlgoNoteItem> safe = (items == null) ? List.of() : List.copyOf(items);
    return new AttemptAlgoNotesResult(safe.size(), safe);
  }
}
