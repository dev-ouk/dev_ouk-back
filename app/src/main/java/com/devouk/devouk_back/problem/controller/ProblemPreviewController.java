package com.devouk.devouk_back.problem.controller;

import com.devouk.devouk_back.problem.dto.ProblemPreviewRequest;
import com.devouk.devouk_back.problem.dto.ProblemPreviewResponse;
import com.devouk.devouk_back.problem.service.ProblemPreviewAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/problem-previews")
@RequiredArgsConstructor
public class ProblemPreviewController {

  private final ProblemPreviewAppService problemPreviewAppService;

  @PostMapping
  public ProblemPreviewResponse preview(@RequestBody @Valid ProblemPreviewRequest request) {
    return problemPreviewAppService.getPreview(request.getUrl());
  }
}
