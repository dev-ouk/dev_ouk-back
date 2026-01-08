package com.devouk.devouk_back.domain.problem;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttemptCreateResult {
  private final UUID attemptUuid;
  private final ProblemSite site;
  private final String siteProblemId;
}
