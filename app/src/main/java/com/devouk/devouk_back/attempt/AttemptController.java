package com.devouk.devouk_back.attempt;

import com.devouk.devouk_back.attempt.dto.AttemptCreateRequest;
import com.devouk.devouk_back.attempt.dto.AttemptCreateResponse;
import com.devouk.devouk_back.attempt.dto.AttemptListResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attempts")
@Validated
public class AttemptController {

  private final AttemptAppService appService;
  private final AttemptQueryAppService queryAppService;

  public AttemptController(AttemptAppService appService, AttemptQueryAppService queryAppService) {
    this.appService = appService;
    this.queryAppService = queryAppService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AttemptCreateResponse create(@RequestBody @Valid AttemptCreateRequest req) {
    var result = appService.create(req);
    return new AttemptCreateResponse(
        result.getAttemptUuid(), result.getSite().name(), result.getSiteProblemId());
  }

  @GetMapping
  public AttemptListResponse getAttempts(
      @RequestParam("site") @NotBlank(message = "site 는 비울 수 없습니다.") String site,
      @RequestParam("siteProblemId")
          @NotBlank(message = "siteProblemId 는 비울 수 없습니다.")
          @Size(max = 64, message = "siteProblemId 는 64자 이하여야 합니다.")
          String siteProblemId) {
    var result = queryAppService.getAll(site, siteProblemId);
    return AttemptListResponse.from(result);
  }
}
