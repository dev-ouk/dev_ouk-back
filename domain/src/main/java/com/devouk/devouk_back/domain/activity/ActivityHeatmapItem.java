package com.devouk.devouk_back.domain.activity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityHeatmapItem {
  private final LocalDate date;
  private final long count;
}
