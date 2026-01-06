package com.devouk.devouk_back.algo_note.detail;

import com.devouk.devouk_back.algo_note.detail.dto.AlgoNoteDetailResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/algo-notes")
@Validated
public class AlgoNoteDetailController {

  private final AlgoNoteDetailAppService appService;

  public AlgoNoteDetailController(AlgoNoteDetailAppService appService) {
    this.appService = appService;
  }

  @GetMapping("/{slug}")
  public AlgoNoteDetailResponse getBySlug(
      @PathVariable("slug")
          @NotBlank(message = "slug은 비울 수 없습니다.")
          @Size(max = 200, message = "slug은 200자 이하여야 합니다.")
          String slug) {
    return appService.getBySlug(slug);
  }
}
