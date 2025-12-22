package com.devouk.devouk_back.domain.algo_note;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAlgoNoteCommand {
  private final String title;
  private final String slug;

  private final String contentJson;
  private final String contentHtml;
  private final String contentText;

  private final AlgoNoteStatus status;
  private final boolean isPublic;
  private final boolean isPin;

  private final List<String> tagSlugs;
}
