package com.devouk.devouk_back.activity.dto;

import com.devouk.devouk_back.domain.activity.ActivityHeatmapItem;
import com.devouk.devouk_back.domain.activity.ActivityHeatmapResult;
import com.devouk.devouk_back.domain.activity.ActivityType;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivitiesGetResponse {

  private final String type;
  private final String from;
  private final String to;
  private final String timeZone;
  private final long total;
  private final List<Item> items;

  public static ActivitiesGetResponse from(
      ActivityType type,
      LocalDate from,
      LocalDate to,
      ZoneId timeZone,
      ActivityHeatmapResult result) {
    DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;
    List<Item> mapped = result.getItems().stream().map(Item::from).toList();

    return new ActivitiesGetResponse(
        type.name(), from.format(df), to.format(df), timeZone.getId(), result.getTotal(), mapped);
  }

  @Getter
  @AllArgsConstructor
  public static class Item {
    private final String date;
    private final long count;

    public static Item from(ActivityHeatmapItem i) {
      return new Item(
          i.getDate() != null ? i.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : null,
          i.getCount());
    }
  }
}
