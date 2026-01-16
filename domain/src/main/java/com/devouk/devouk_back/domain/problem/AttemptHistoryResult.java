package com.devouk.devouk_back.domain.problem;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttemptHistoryResult {
  private final long total;
  private final List<AttemptHistoryItem> items;
}
