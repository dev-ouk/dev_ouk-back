package com.devouk.devouk_back.attempt.algo_note;

import com.devouk.devouk_back.attempt.algo_note.dto.AttemptAlgoNotesReplaceRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attempts")
@Validated
public class AttemptAlgoNoteLinkController {

  private final AttemptAlgoNoteLinkAppService appService;

  public AttemptAlgoNoteLinkController(AttemptAlgoNoteLinkAppService appService) {
    this.appService = appService;
  }

  @PutMapping("/{attemptUuid}/algo-notes")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void replaceAttemptAlgoNotes(
      @PathVariable("attemptUuid") UUID attemptUuid,
      @RequestBody @Valid AttemptAlgoNotesReplaceRequest req) {

    appService.replace(attemptUuid, req);
  }
}
