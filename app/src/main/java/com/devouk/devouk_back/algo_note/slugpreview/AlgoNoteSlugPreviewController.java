package com.devouk.devouk_back.algo_note.slugpreview;

import com.devouk.devouk_back.algo_note.slugpreview.dto.AlgoNoteSlugPreviewRequest;
import com.devouk.devouk_back.algo_note.slugpreview.dto.AlgoNoteSlugPreviewResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/algo-notes")
public class AlgoNoteSlugPreviewController {
  private final AlgoNoteSlugPreviewAppService appService;

  public AlgoNoteSlugPreviewController(AlgoNoteSlugPreviewAppService appService) {
    this.appService = appService;
  }

  @PostMapping("/slug-preview")
  public AlgoNoteSlugPreviewResponse slugPreview(
      @RequestBody @Valid AlgoNoteSlugPreviewRequest req) {
    return appService.preview(req.getTitle());
  }
}
