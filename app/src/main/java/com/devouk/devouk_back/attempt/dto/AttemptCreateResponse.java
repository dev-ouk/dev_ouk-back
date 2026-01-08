package com.devouk.devouk_back.attempt.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttemptCreateResponse {
  private final UUID attemptUuid;
  private final String site;
  private final String siteProblemId;
}
