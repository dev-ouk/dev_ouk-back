package com.devouk.devouk_back.algo_note.list.dto;

import com.devouk.devouk_back.domain.algo_note.AlgoNoteListItem;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteListPage;
import com.devouk.devouk_back.domain.taxonomy.Term;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlgoNotesResponse {
  private final List<Item> items;
  private final int size;
  private final boolean hasNext;
  private final String nextCursor;
  private final String sort;

  public static AlgoNotesResponse from(AlgoNoteListPage page, String nextCursor, String sortParam) {
    List<Item> mapped = page.getItems().stream().map(Item::from).toList();
    return new AlgoNotesResponse(mapped, page.getSize(), page.isHasNext(), nextCursor, sortParam);
  }

  public static class Item {
    private final String slug;
    private final String title;
    private final boolean isPin;
    private final String createdAt;
    private final Map<String, TaxonomyNode> taxonomies;

    public static Item from(AlgoNoteListItem item) {
      String createdAt =
          item.getCreatedAt() != null
              ? item.getCreatedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
              : null;

      Map<String, TaxonomyNode> mapped =
          item.getTaxonomies().entrySet().stream()
              .collect(
                  java.util.stream.Collectors.toMap(
                      Map.Entry::getKey, e -> TaxonomyNode.from(e.getValue())));

      return new Item(item.getSlug(), item.getTitle(), item.isPin(), createdAt, mapped);
    }

    public Item(
        String slug,
        String title,
        boolean isPin,
        String createdAt,
        Map<String, TaxonomyNode> taxonomies) {
      this.slug = slug;
      this.title = title;
      this.isPin = isPin;
      this.createdAt = createdAt;
      this.taxonomies = taxonomies;
    }

    public String getTitle() {
      return title;
    }

    public boolean isPin() {
      return isPin;
    }

    public String getCreatedAt() {
      return createdAt;
    }

    public Map<String, TaxonomyNode> getTaxonomies() {
      return taxonomies;
    }
  }

  public static class TaxonomyNode {
    private final List<TermItem> terms;

    public static TaxonomyNode from(List<Term> terms) {
      List<TermItem> mapped = terms.stream().map(TermItem::from).toList();
      return new TaxonomyNode(mapped);
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

    public static TermItem from(Term t) {
      return new TermItem(t.getSlug(), t.getName());
    }

    public TermItem(String slug, String name) {
      this.slug = slug;
      this.name = name;
    }

    public String getSlug() {
      return slug;
    }

    public String getName() {
      return name;
    }
  }
}
