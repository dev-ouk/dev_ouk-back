package com.devouk.devouk_back.infra.activity;

import java.time.LocalDate;

public class ActivityHeatmapRow {
  private final LocalDate date;
  private final long count;

  public ActivityHeatmapRow(LocalDate date, long count) {
    this.date = date;
    this.count = count;
  }

  public LocalDate getDate() {
    return date;
  }

  public long getCount() {
    return count;
  }
}
