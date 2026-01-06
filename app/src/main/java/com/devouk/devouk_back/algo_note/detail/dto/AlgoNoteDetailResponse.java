package com.devouk.devouk_back.algo_note.detail.dto;

import com.devouk.devouk_back.domain.algo_note.AlgoNoteDetail;
import com.devouk.devouk_back.domain.taxonomy.Term;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlgoNoteDetailResponse {

  private final String title;
  private final String slug;

  private final String contentJson;
  private final String contentHtml;
  private final String contentText;

  private final String status;
  private final boolean isPublic;
  private final boolean isPin;

  private final String createdAt;
  private final String updatedAt;
  private final String publishedAt;

  private final Map<String, TaxonomyNode> taxonomies;

  public static AlgoNoteDetailResponse from(AlgoNoteDetail d) {
    return new AlgoNoteDetailResponse(
        d.getTitle(),
        d.getSlug(),
        d.getContentJson(),
        d.getContentHtml(),
        d.getContentText(),
        d.getStatus().name(),
        d.isPublic(),
        d.isPin(),
        format(d.getCreatedAt()),
        format(d.getUpdatedAt()),
        format(d.getPublishedAt()),
        d.getTaxonomies().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> TaxonomyNode.from(e.getValue()))));
  }

  private static String format(OffsetDateTime dt) {
    return dt == null ? null : dt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  public static class TaxonomyNode {
    private final List<TermItem> terms;

    public static TaxonomyNode from(List<Term> terms) {
      return new TaxonomyNode(terms.stream().map(TermItem::from).toList());
    }

    public TaxonomyNode(List<TermItem> terms) {
      this.terms = terms;
    }

    public List<TermItem> getTerms() {
      return terms;
    }
  }

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

    public String getSlug() {
      return slug;
    }

    public String getName() {
      return name;
    }
  }
}
