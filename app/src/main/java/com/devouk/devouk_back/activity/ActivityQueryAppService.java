package com.devouk.devouk_back.activity;

import com.devouk.devouk_back.activity.dto.ActivitiesGetResponse;
import com.devouk.devouk_back.domain.activity.*;
import com.devouk.devouk_back.domain.common.exception.InvalidActivityDateRangeException;
import com.devouk.devouk_back.domain.common.exception.InvalidActivityGroupByException;
import com.devouk.devouk_back.domain.common.exception.InvalidActivityTimeZoneException;
import com.devouk.devouk_back.domain.common.exception.InvalidActivityTypeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ActivityQueryAppService {

  private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Seoul");

  private final ActivityHeatmapQueryPort queryPort;

  public ActivityQueryAppService(ActivityHeatmapQueryPort queryPort) {
    this.queryPort = queryPort;
  }

  public ActivitiesGetResponse getHeatmap(
      String typeRaw, LocalDate from, LocalDate to, String groupByRaw, String timeZoneRaw) {
    ActivityType type = parseType(typeRaw);
    ActivityGroupBy groupBy = parseGroupBy(groupByRaw);
    ZoneId zone = parseZone(timeZoneRaw);

    validateRange(from, to);

    if (groupBy != ActivityGroupBy.DAY) {
      throw new InvalidActivityGroupByException(groupByRaw);
    }

    ActivityHeatmapQuery query = new ActivityHeatmapQuery(type, from, to, groupBy, zone);
    ActivityHeatmapResult raw = queryPort.getHeatmap(query);

    ActivityHeatmapResult filled = fillMissingDates(from, to, raw);

    return ActivitiesGetResponse.from(type, from, to, zone, filled);
  }

  private ActivityType parseType(String raw) {
    try {
      ActivityType t = ActivityType.parse(raw);
      if (t == null) {
        throw new IllegalArgumentException();
      }
      return t;
    } catch (Exception e) {
      throw new InvalidActivityTypeException(raw);
    }
  }

  private ActivityGroupBy parseGroupBy(String raw) {
    try {
      return ActivityGroupBy.parseOrDefault(raw);
    } catch (Exception e) {
      throw new InvalidActivityGroupByException(raw);
    }
  }

  private ZoneId parseZone(String raw) {
    if (raw == null || raw.isBlank()) {
      return DEFAULT_ZONE;
    }
    try {
      return ZoneId.of(raw.trim());
    } catch (Exception e) {
      throw new InvalidActivityTimeZoneException(raw);
    }
  }

  private void validateRange(LocalDate from, LocalDate to) {
    if (from == null || to == null) {
      throw new InvalidActivityDateRangeException();
    }
    if (from.isAfter(to)) {
      throw new InvalidActivityDateRangeException();
    }
  }

  private ActivityHeatmapResult fillMissingDates(
      LocalDate from, LocalDate to, ActivityHeatmapResult raw) {
    Map<LocalDate, Long> map = new HashMap<>();
    for (ActivityHeatmapItem i : raw.getItems()) {
      if (i.getDate() != null) {
        map.put(i.getDate(), i.getCount());
      }
    }

    List<ActivityHeatmapItem> items = new ArrayList<>();
    LocalDate d = from;
    while (!d.isAfter(to)) {
      long cnt = map.getOrDefault(d, 0L);
      items.add(new ActivityHeatmapItem(d, cnt));
      d = d.plusDays(1);
    }

    return ActivityHeatmapResult.of(items);
  }
}
