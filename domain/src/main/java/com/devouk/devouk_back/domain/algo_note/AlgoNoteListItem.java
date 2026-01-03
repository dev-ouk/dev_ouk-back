package com.devouk.devouk_back.domain.algo_note;

import com.devouk.devouk_back.domain.taxonomy.Term;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlgoNoteListItem {
  private final String slug;
  private final String title;
  private final boolean isPin;
  private final OffsetDateTime createdAt;

  private final Map<String, List<Term>> taxonomies;
}
