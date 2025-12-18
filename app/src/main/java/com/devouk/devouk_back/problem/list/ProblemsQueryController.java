package com.devouk.devouk_back.problem.list;

import com.devouk.devouk_back.problem.list.dto.ProblemsResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/problems")
@Validated
public class ProblemsQueryController {

  private final ProblemListAppService appService;

  public ProblemsQueryController(ProblemListAppService appService) {
    this.appService = appService;
  }

  @GetMapping
  public ProblemsResponse getProblems(
      @RequestParam(value = "q", required = false) String q,
      @RequestParam(value = "sites", required = false) List<String> sites,
      @RequestParam(value = "difficultyMin", required = false) Integer difficultyMin,
      @RequestParam(value = "difficultyMax", required = false) Integer difficultyMax,
      @RequestParam(value = "tagSlugs", required = false) List<String> tagSlugs,
      @RequestParam(value = "finalVerdict", required = false) String finalVerdict,
      @RequestParam(value = "attemptedFrom", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          OffsetDateTime attemptedFrom,
      @RequestParam(value = "attemptedTo", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          OffsetDateTime attemptedTo,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "size", required = false)
          @Min(value = 1, message = "size 는 1 이상이어야 합니다.")
          @Max(value = 100, message = "size 는 100 이하이어야 합니다.")
          Integer size,
      @RequestParam(value = "cursor", required = false) String cursor) {

    return appService.search(
        q,
        sites,
        difficultyMin,
        difficultyMax,
        tagSlugs,
        finalVerdict,
        attemptedFrom,
        attemptedTo,
        sort,
        size,
        cursor);
  }
}
