package com.devouk.devouk_back.algo_note.detail;

import com.devouk.devouk_back.algo_note.detail.dto.AlgoNoteDetailResponse;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteDetailQueryPort;
import com.devouk.devouk_back.domain.common.exception.AlgoNoteNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AlgoNoteDetailAppService {

  private final AlgoNoteDetailQueryPort queryPort;

  public AlgoNoteDetailAppService(AlgoNoteDetailQueryPort queryPort) {
    this.queryPort = queryPort;
  }

  public AlgoNoteDetailResponse getBySlug(String slug) {
    String s = normalizeSlug(slug);

    return queryPort
        .findBySlug(s)
        .map(AlgoNoteDetailResponse::from)
        .orElseThrow(() -> new AlgoNoteNotFoundException(s));
  }

  private String normalizeSlug(String slug) {
    if (slug == null) {
      return null;
    }
    String t = slug.trim();
    return t.isEmpty() ? null : t;
  }
}
