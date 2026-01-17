package com.devouk.devouk_back.domain.activity;

import java.time.LocalDate;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityHeatmapQuery {
  private final ActivityType type;
  private final LocalDate from;
  private final LocalDate to;
  private final ActivityGroupBy groupBy;
  private final ZoneId timeZone;
}
