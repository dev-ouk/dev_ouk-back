package com.devouk.devouk_back.algo_note;

import com.devouk.devouk_back.algo_note.dto.AlgoNoteCreateRequest;
import com.devouk.devouk_back.algo_note.dto.AlgoNoteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/algo-notes")
@RequiredArgsConstructor
public class AlgoNoteController {

  private final AlgoNoteAppService appService;

  @PostMapping
  public AlgoNoteResponse create(@RequestBody @Valid AlgoNoteCreateRequest request) {
    return AlgoNoteResponse.from(appService.create(request));
  }
}
