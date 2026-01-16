package com.devouk.devouk_back.domain.problem;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplaceAttemptAlgoNotesCommand {
  private final UUID attemptUuid;
  private final List<String> algoNoteSlugs;
}
