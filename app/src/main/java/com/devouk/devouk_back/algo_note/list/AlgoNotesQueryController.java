package com.devouk.devouk_back.algo_note.list;

import com.devouk.devouk_back.algo_note.list.dto.AlgoNotesResponse;
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
@RequestMapping("/api/v1/algo-notes")
@Validated
public class AlgoNotesQueryController {

  private final AlgoNoteListAppService appService;

  public AlgoNotesQueryController(AlgoNoteListAppService appService) {
    this.appService = appService;
  }

  @GetMapping
  public AlgoNotesResponse getAlgoNotes(
      @RequestParam(value = "q", required = false) String q,
      @RequestParam(value = "tagSlugs", required = false) List<String> tagSlugs,
      @RequestParam(value = "createdAtFrom", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          OffsetDateTime createdAtFrom,
      @RequestParam(value = "createdAtTo", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          OffsetDateTime createdAtTo,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "size", required = false)
          @Min(value = 1, message = "size 는 1 이상이어야 합니다.")
          @Max(value = 100, message = "size 는 100 이하이어야 합니다.")
          Integer size,
      @RequestParam(value = "cursor", required = false) String cursor) {

    return appService.search(q, tagSlugs, createdAtFrom, createdAtTo, sort, size, cursor);
  }
}
