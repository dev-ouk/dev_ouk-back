package com.devouk.devouk_back.attempt.algo_note;

import com.devouk.devouk_back.attempt.algo_note.dto.AttemptAlgoNotesGetResponse;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/attempts")
@Validated
public class AttemptAlgoNoteQueryController {

  private final AttemptAlgoNoteQueryAppService appService;

  public AttemptAlgoNoteQueryController(AttemptAlgoNoteQueryAppService appService) {
    this.appService = appService;
  }

  @GetMapping("/{attemptUuid}/algo-notes")
  public AttemptAlgoNotesGetResponse getAttemptAlgoNotes(
      @PathVariable("attemptUuid") UUID attemptUuid) {
    return appService.getAll(attemptUuid);
  }
}
