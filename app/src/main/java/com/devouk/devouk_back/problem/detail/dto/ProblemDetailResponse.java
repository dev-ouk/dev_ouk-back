package com.devouk.devouk_back.problem.detail.dto;

import com.devouk.devouk_back.domain.problem.ProblemDetail;
import com.devouk.devouk_back.domain.problem.ProblemLastAttemptSummary;
import com.devouk.devouk_back.domain.taxonomy.Term;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemDetailResponse {

  private final String site;
  private final String siteProblemId;
  private final String title;
  private final Integer difficulty;
  private final String url;

  private final LastAttempt lastAttempt;
  private final Map<String, TaxonomyNode> taxonomies;

  public static ProblemDetailResponse from(ProblemDetail d) {
    LastAttempt last = d.getLastAttempt() != null ? LastAttempt.from(d.getLastAttempt()) : null;

    Map<String, TaxonomyNode> mapped =
        d.getTaxonomies().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> TaxonomyNode.from(e.getValue())));

    return new ProblemDetailResponse(
        d.getSite().name(),
        d.getSiteProblemId(),
        d.getTitle(),
        d.getDifficulty(),
        d.getUrl(),
        last,
        mapped);
  }

  @Getter
  public static class LastAttempt {
    private final String verdict;
    private final String attemptedAt;

    public static LastAttempt from(ProblemLastAttemptSummary s) {
      return new LastAttempt(
          s.getVerdict().name(),
          s.getAttemptedAt() != null
              ? s.getAttemptedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
              : null);
    }

    public LastAttempt(String verdict, String attemptedAt) {
      this.verdict = verdict;
      this.attemptedAt = attemptedAt;
    }
  }

  @Getter
  public static class TaxonomyNode {
    private final List<TermItem> terms;

    public static TaxonomyNode from(List<Term> terms) {
      return new TaxonomyNode(terms.stream().map(TermItem::from).toList());
    }

    public TaxonomyNode(List<TermItem> terms) {
      this.terms = terms;
    }
  }

  @Getter
  public static class TermItem {
    private final String slug;
    private final String name;

    public TermItem(String slug, String name) {
      this.slug = slug;
      this.name = name;
    }

    public static TermItem from(Term t) {
      return new TermItem(t.getSlug(), t.getName());
    }
  }
}
