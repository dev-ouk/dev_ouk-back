package com.devouk.devouk_back.problem.dto;

import com.devouk.devouk_back.domain.problem.ProblemPreview;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemPreviewResponse {

  private String site;
  private String siteProblemId;
  private String title;
  private String url;
  private Integer difficulty;
  private Map<String, TaxonomySuggestionResponse> taxonomies;

  public static ProblemPreviewResponse from(ProblemPreview preview) {
    Map<String, TaxonomySuggestionResponse> mapped =
        preview.getSuggestedTaxonomyTermSlugs().entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey, e -> new TaxonomySuggestionResponse(e.getValue())));

    return new ProblemPreviewResponse(
        preview.getSite().name(),
        preview.getSiteProblemId(),
        preview.getTitle(),
        preview.getUrl(),
        preview.getDifficulty(),
        mapped);
  }

  @Getter
  @AllArgsConstructor
  public static class TaxonomySuggestionResponse {
    private List<String> suggestedTermSlugs;
  }
}
