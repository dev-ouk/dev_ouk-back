package com.devouk.devouk_back.algo_note.slugpreview;

import com.devouk.devouk_back.algo_note.slugpreview.dto.AlgoNoteSlugPreviewResponse;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteSlugService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AlgoNoteSlugPreviewAppService {

  private final AlgoNoteSlugService slugService;

  public AlgoNoteSlugPreviewAppService(AlgoNoteSlugService slugService) {
    this.slugService = slugService;
  }

  public AlgoNoteSlugPreviewResponse preview(String title) {
    return AlgoNoteSlugPreviewResponse.from(slugService.preview(title));
  }
}
