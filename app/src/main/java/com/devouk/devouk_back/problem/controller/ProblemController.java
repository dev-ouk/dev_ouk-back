package com.devouk.devouk_back.problem.controller;

import com.devouk.devouk_back.problem.dto.ProblemCreateRequest;
import com.devouk.devouk_back.problem.dto.ProblemResponse;
import com.devouk.devouk_back.problem.service.ProblemAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/problems")
@RequiredArgsConstructor
public class ProblemController {

  private final ProblemAppService problemAppService;

  @PostMapping
  public ProblemResponse create(@RequestBody @Valid ProblemCreateRequest request) {
    return ProblemResponse.from(problemAppService.createProblem(request));
  }
}
