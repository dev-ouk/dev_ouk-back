package com.devouk.devouk_back.taxonomy.controller;

import com.devouk.devouk_back.taxonomy.dto.TaxonomyTermsResponse;
import com.devouk.devouk_back.taxonomy.service.TaxonomyAppService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/taxonomies")
@Validated
@RequiredArgsConstructor
public class TaxonomyController {

  private final TaxonomyAppService taxonomyAppService;

  @GetMapping("/{taxonomy}/terms")
  public TaxonomyTermsResponse getTerms(
      @PathVariable("taxonomy") String taxonomy,
      @RequestParam(value = "q", required = false) String q) {
    return taxonomyAppService.getTerms(taxonomy, Optional.ofNullable(q));
  }
}
