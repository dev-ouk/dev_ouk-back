package com.devouk.devouk_back.infra.algo_note;

import com.devouk.devouk_back.domain.algo_note.*;
import com.devouk.devouk_back.domain.taxonomy.Term;
import com.devouk.devouk_back.infra.taxonomy.TermEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class JpaAlgoNoteListQueryRepository implements AlgoNoteListQueryPort {

  private static final String ALGO_TAXONOMY_CODE = "algo";

  private final EntityManager em;

  public JpaAlgoNoteListQueryRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public AlgoNoteListPage search(AlgoNoteListQuery q) {
    int pageSize = clampSize(q.getSize());

    List<Long> ids = fetchNoteIds(q, pageSize + 1);

    boolean hasNext = ids.size() > pageSize;
    if (hasNext) {
      ids = ids.subList(0, pageSize);
    }

    if (ids.isEmpty()) {
      return new AlgoNoteListPage(List.of(), pageSize, false, null, null);
    }

    List<AlgoNoteEntity> loaded = fetchNotesWithTerms(ids);

    Map<Long, AlgoNoteEntity> byId =
        loaded.stream().collect(Collectors.toMap(AlgoNoteEntity::getId, e -> e));

    List<AlgoNoteListItem> items = new ArrayList<>(ids.size());
    for (Long id : ids) {
      AlgoNoteEntity e = byId.get(id);
      if (e != null) {
        items.add(toItem(e));
      }
    }

    AlgoNoteEntity last = byId.get(ids.get(ids.size() - 1));
    OffsetDateTime nextCreatedAt = last != null ? last.getCreatedAt() : null;
    Long nextId = last != null ? last.getId() : null;

    return new AlgoNoteListPage(items, pageSize, hasNext, nextCreatedAt, nextId);
  }

  private int clampSize(int requestedSize) {
    return Math.max(1, Math.min(requestedSize, 100));
  }

  private List<Long> fetchNoteIds(AlgoNoteListQuery q, int limit) {
    StringBuilder jpql = new StringBuilder();
    Map<String, Object> params = new HashMap<>();

    boolean filterByTags = q.getTagSlugs() != null && !q.getTagSlugs().isEmpty();

    jpql.append("select n.id ");
    jpql.append("  from AlgoNoteEntity n ");
    jpql.append(" where 1=1 ");

    appendQFilter(jpql, params, q);
    appendCreatedAtRangeFilter(jpql, params, q);
    appendTagExistsFilter(jpql, params, q, filterByTags);
    appendCursorFilter(jpql, params, q);

    appendOrderBy(jpql, q.getSort());

    TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
    params.forEach(query::setParameter);
    query.setMaxResults(limit);
    return query.getResultList();
  }

  private void appendQFilter(StringBuilder jpql, Map<String, Object> params, AlgoNoteListQuery q) {
    if (q.getQ() == null || q.getQ().isBlank()) {
      return;
    }

    // 제목 부분검색 + slug prefix 검색도 같이(원하면 제거 가능)
    jpql.append(" and ( lower(n.title) like concat('%', lower(:q), '%') ");
    jpql.append("       or n.slug like concat(:q, '%') ) ");
    params.put("q", q.getQ().trim());
  }

  private void appendCreatedAtRangeFilter(
      StringBuilder jpql, Map<String, Object> params, AlgoNoteListQuery q) {

    if (q.getCreatedAtFrom() != null) {
      jpql.append(" and n.createdAt >= :from ");
      params.put("from", q.getCreatedAtFrom());
    }
    if (q.getCreatedAtTo() != null) {
      jpql.append(" and n.createdAt <= :to ");
      params.put("to", q.getCreatedAtTo());
    }
  }

  private void appendTagExistsFilter(
      StringBuilder jpql, Map<String, Object> params, AlgoNoteListQuery q, boolean filterByTags) {

    if (!filterByTags) {
      return;
    }

    jpql.append(" and exists (");
    jpql.append("   select 1 ");
    jpql.append("     from n.terms t ");
    jpql.append("     join t.taxonomy tx ");
    jpql.append("    where tx.code = :algoCode ");
    jpql.append("      and t.slug in :tagSlugs ");
    jpql.append(" ) ");

    params.put("algoCode", ALGO_TAXONOMY_CODE);
    params.put("tagSlugs", q.getTagSlugs());
  }

  private void appendCursorFilter(
      StringBuilder jpql, Map<String, Object> params, AlgoNoteListQuery q) {

    if (q.getCursorNoteId() == null || q.getCursorCreatedAt() == null) {
      return;
    }

    switch (q.getSort()) {
      case CREATED_AT_DESC -> {
        jpql.append(" and ( n.createdAt < :cursorCreatedAt ");
        jpql.append("        or (n.createdAt = :cursorCreatedAt and n.id < :cursorId) ) ");
        params.put("cursorCreatedAt", q.getCursorCreatedAt());
        params.put("cursorId", q.getCursorNoteId());
      }
      case CREATED_AT_ASC -> {
        jpql.append(" and ( n.createdAt > :cursorCreatedAt ");
        jpql.append("        or (n.createdAt = :cursorCreatedAt and n.id > :cursorId) ) ");
        params.put("cursorCreatedAt", q.getCursorCreatedAt());
        params.put("cursorId", q.getCursorNoteId());
      }
    }
  }

  private void appendOrderBy(StringBuilder jpql, AlgoNoteListSortOption sort) {
    jpql.append(" order by ");
    switch (sort) {
      case CREATED_AT_DESC -> jpql.append(" n.createdAt desc, n.id desc ");
      case CREATED_AT_ASC -> jpql.append(" n.createdAt asc, n.id asc ");
    }
  }

  private List<AlgoNoteEntity> fetchNotesWithTerms(List<Long> ids) {
    // pagination은 ids로 이미 끝났기 때문에 fetch join 안전
    // taxonomy 확장성을 위해 taxonomy도 fetch
    return em.createQuery(
            """
                        select distinct n
                          from AlgoNoteEntity n
                          left join fetch n.terms t
                          left join fetch t.taxonomy tx
                         where n.id in :ids
                        """,
            AlgoNoteEntity.class)
        .setParameter("ids", ids)
        .getResultList();
  }

  private AlgoNoteListItem toItem(AlgoNoteEntity e) {
    Map<String, List<Term>> taxonomies = groupTermsByTaxonomy(e.getTerms());

    // 응답 스펙에서 taxonomies.algo는 항상 존재하게(없으면 빈 terms)
    taxonomies.putIfAbsent(ALGO_TAXONOMY_CODE, List.of());

    return new AlgoNoteListItem(e.getSlug(), e.getTitle(), e.isPin(), e.getCreatedAt(), taxonomies);
  }

  private Map<String, List<Term>> groupTermsByTaxonomy(Set<TermEntity> terms) {
    if (terms == null || terms.isEmpty()) {
      return new HashMap<>();
    }

    Map<String, List<Term>> grouped = new HashMap<>();
    for (TermEntity t : terms) {
      if (t.getTaxonomy() == null || t.getTaxonomy().getCode() == null) {
        continue;
      }

      String code = t.getTaxonomy().getCode();

      // 지금 스펙은 algo만 내려도 되지만, 확장성을 위해 taxonomy별 grouping 유지
      grouped.computeIfAbsent(code, k -> new ArrayList<>()).add(new Term(t.getSlug(), t.getName()));
    }

    // terms 정렬(선택): name asc
    for (var entry : grouped.entrySet()) {
      entry
          .getValue()
          .sort(Comparator.comparing(Term::getName, Comparator.nullsLast(String::compareTo)));
    }

    return grouped;
  }
}
