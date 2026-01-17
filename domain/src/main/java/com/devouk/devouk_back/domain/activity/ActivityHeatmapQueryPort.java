package com.devouk.devouk_back.domain.activity;

public interface ActivityHeatmapQueryPort {
  ActivityHeatmapResult getHeatmap(ActivityHeatmapQuery query);
}
