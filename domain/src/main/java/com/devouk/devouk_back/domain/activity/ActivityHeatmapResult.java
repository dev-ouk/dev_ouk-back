package com.devouk.devouk_back.domain.activity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityHeatmapResult {
  private final long total;
  private final List<ActivityHeatmapItem> items;

  public static ActivityHeatmapResult of(List<ActivityHeatmapItem> items) {
    List<ActivityHeatmapItem> safe = (items == null) ? List.of() : List.copyOf(items);
    long total = safe.stream().mapToLong(ActivityHeatmapItem::getCount).sum();
    return new ActivityHeatmapResult(total, safe);
  }
}
