package com.devouk.devouk_back.infra.algo_note;

import com.devouk.devouk_back.domain.algo_note.AlgoNoteDetail;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteDetailQueryPort;
import com.devouk.devouk_back.domain.taxonomy.Term;
import com.devouk.devouk_back.infra.taxonomy.TermEntity;
import java.util.*;

public class JpaAlgoNoteDetailQueryRepository implements AlgoNoteDetailQueryPort {

  private static final String ALGO_TAXONOMY_CODE = "algo";

  private final AlgoNoteJpaRepository repo;

  public JpaAlgoNoteDetailQueryRepository(AlgoNoteJpaRepository repo) {
    this.repo = repo;
  }

  @Override
  public Optional<AlgoNoteDetail> findBySlug(String slug) {
    return repo.findOneBySlugWithTerms(slug).map(this::toDetail);
  }

  private AlgoNoteDetail toDetail(AlgoNoteEntity e) {
    Map<String, List<Term>> taxonomies = groupTermsByTaxonomy(e.getTerms());
    taxonomies.putIfAbsent(ALGO_TAXONOMY_CODE, List.of());

    return new AlgoNoteDetail(
        e.getTitle(),
        e.getSlug(),
        e.getContentJson(),
        e.getContentHtml(),
        e.getContentText(),
        e.getStatus(),
        e.isPublic(),
        e.isPin(),
        e.getCreatedAt(),
        e.getUpdatedAt(),
        e.getPublishedAt(),
        taxonomies);
  }

  private Map<String, List<Term>> groupTermsByTaxonomy(Set<TermEntity> terms) {
    Map<String, List<Term>> grouped = new HashMap<>();
    if (terms == null || terms.isEmpty()) {
      return grouped;
    }

    for (TermEntity t : terms) {
      if (t.getTaxonomy() == null || t.getTaxonomy().getCode() == null) {
        continue;
      }
      String code = t.getTaxonomy().getCode();
      grouped.computeIfAbsent(code, k -> new ArrayList<>()).add(new Term(t.getSlug(), t.getName()));
    }

    for (var entry : grouped.entrySet()) {
      entry
          .getValue()
          .sort(Comparator.comparing(Term::getName, Comparator.nullsLast(String::compareTo)));
    }

    return grouped;
  }
}
