package com.devouk.devouk_back.activity;

import com.devouk.devouk_back.activity.dto.ActivitiesGetResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/activities")
@Validated
public class ActivityController {

  private final ActivityQueryAppService appService;

  public ActivityController(ActivityQueryAppService appService) {
    this.appService = appService;
  }

  @GetMapping
  public ActivitiesGetResponse getActivities(
      @RequestParam("type") @NotBlank(message = "type 은 비울 수 없습니다.") String type,
      @RequestParam("from")
          @NotNull(message = "from 은 비울 수 없습니다.")
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate from,
      @RequestParam("to")
          @NotNull(message = "to 은 비울 수 없습니다.")
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate to,
      @RequestParam(value = "groupBy", required = false) String groupBy,
      @RequestParam(value = "timeZone", required = false) String timeZone) {
    return appService.getHeatmap(type, from, to, groupBy, timeZone);
  }
}
