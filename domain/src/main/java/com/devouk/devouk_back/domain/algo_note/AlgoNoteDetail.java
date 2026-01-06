package com.devouk.devouk_back.domain.algo_note;

import com.devouk.devouk_back.domain.taxonomy.Term;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlgoNoteDetail {

  private final String title;
  private final String slug;

  private final String contentJson;
  private final String contentHtml;
  private final String contentText;

  private final AlgoNoteStatus status;
  private final boolean isPublic;
  private final boolean isPin;

  private final OffsetDateTime createdAt;
  private final OffsetDateTime updatedAt;
  private final OffsetDateTime publishedAt;

  private final Map<String, List<Term>> taxonomies;
}
