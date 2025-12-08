package com.devouk.devouk_back.problem.candidate;

import com.devouk.devouk_back.problem.candidate.dto.ProblemCandidatesResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/problem-candidates")
@Validated
public class ProblemCandidateController {

  private final ProblemCandidateAppService appService;

  public ProblemCandidateController(ProblemCandidateAppService appService) {
    this.appService = appService;
  }

  @GetMapping
  public ProblemCandidatesResponse getProblemCandidates(
      @RequestParam(value = "q", required = false) String q,
      @RequestParam(value = "sites", required = false) List<String> sites,
      @RequestParam(value = "size", required = false)
          @Min(value = 1, message = "size 는 1 이상이어야 합니다.")
          @Max(value = 100, message = "size 는 100 이하이어야 합니다.")
          Integer size,
      @RequestParam(value = "cursor", required = false) String cursor) {

    return appService.search(q, sites, size, cursor);
  }
}
