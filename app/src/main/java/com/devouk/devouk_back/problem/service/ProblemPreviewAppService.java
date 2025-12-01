package com.devouk.devouk_back.problem.service;

import com.devouk.devouk_back.domain.problem.ProblemPreviewPort;
import com.devouk.devouk_back.problem.dto.ProblemPreviewResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProblemPreviewAppService {

  private final ProblemPreviewPort problemPreviewPort;

  public ProblemPreviewAppService(ProblemPreviewPort problemPreviewPort) {
    this.problemPreviewPort = problemPreviewPort;
  }

  public ProblemPreviewResponse getPreview(String url) {
    var preview = problemPreviewPort.getPreview(url);
    return ProblemPreviewResponse.from(preview);
  }
}
