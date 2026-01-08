package com.devouk.devouk_back.attempt;

import com.devouk.devouk_back.attempt.dto.AttemptCreateRequest;
import com.devouk.devouk_back.attempt.dto.AttemptCreateResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attempts")
public class AttemptController {

  private final AttemptAppService appService;

  public AttemptController(AttemptAppService appService) {
    this.appService = appService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AttemptCreateResponse create(@RequestBody @Valid AttemptCreateRequest req) {
    var result = appService.create(req);
    return new AttemptCreateResponse(
        result.getAttemptUuid(), result.getSite().name(), result.getSiteProblemId());
  }
}
