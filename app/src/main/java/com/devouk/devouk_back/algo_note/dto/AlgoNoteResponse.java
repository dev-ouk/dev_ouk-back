package com.devouk.devouk_back.algo_note.dto;

import com.devouk.devouk_back.domain.algo_note.AlgoNote;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlgoNoteResponse {

  private final String title;
  private final String slug;

  // TipTap JSON serialized string
  private final String contentJson;
  private final String contentHtml;
  private final String contentText;

  private final String status;
  private final boolean isPublic;
  private final boolean isPin;

  private final List<String> tagSlugs;

  public static AlgoNoteResponse from(AlgoNote note) {
    return new AlgoNoteResponse(
        note.getTitle(),
        note.getSlug(),
        note.getContentJson(),
        note.getContentHtml(),
        note.getContentText(),
        note.getStatus().name(),
        note.isPublic(),
        note.isPin(),
        note.getTagSlugs());
  }
}
