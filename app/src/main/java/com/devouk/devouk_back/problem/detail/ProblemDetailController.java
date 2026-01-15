package com.devouk.devouk_back.problem.detail;

import com.devouk.devouk_back.problem.detail.dto.ProblemDetailResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/problems")
@Validated
public class ProblemDetailController {

  private final ProblemDetailAppService appService;

  public ProblemDetailController(ProblemDetailAppService appService) {
    this.appService = appService;
  }

  @GetMapping("/{site}/{siteProblemId}")
  public ProblemDetailResponse getProblemDetail(
      @PathVariable("site") @NotBlank(message = "site 는 비울 수 없습니다.") String site,
      @PathVariable("siteProblemId")
          @NotBlank(message = "siteProblemId 는 비울 수 없습니다.")
          @Size(max = 64, message = "siteProblemId 는 64자 이하여야 합니다.")
          String siteProblemId) {
    return appService.get(site, siteProblemId);
  }
}
