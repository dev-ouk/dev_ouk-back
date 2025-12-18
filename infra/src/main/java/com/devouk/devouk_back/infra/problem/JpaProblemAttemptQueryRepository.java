package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.problem.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JpaProblemAttemptQueryRepository implements ProblemAttemptQueryPort {

  private static final String ALGO_TAXONOMY_CODE = "algo";

  private final EntityManager em;

  public JpaProblemAttemptQueryRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public ProblemCandidatePage search(ProblemAttemptQuery q) {
    int pageSize = clampSize(q.getSize());

    QuerySpec spec = buildQuerySpec(q);
    List<AttemptEntity> fetched = execute(spec, pageSize + 1);

    Slice slice = slice(fetched, pageSize);
    return toPage(slice, pageSize);
  }

  private int clampSize(int requestedSize) {
    return Math.max(1, Math.min(requestedSize, 100));
  }

  private QuerySpec buildQuerySpec(ProblemAttemptQuery q) {
    StringBuilder jpql = new StringBuilder();
    Map<String, Object> params = new HashMap<>();

    boolean filterByTags = hasTags(q);
    appendSelectFrom(jpql, filterByTags);
    appendLatestAttemptOnly(jpql);

    appendQFilter(jpql, params, q);
    appendSitesFilter(jpql, params, q);
    appendDifficultyFilter(jpql, params, q);
    appendTagFilter(jpql, params, q, filterByTags);
    appendFinalVerdictFilter(jpql, params, q);
    appendAttemptedRangeFilter(jpql, params, q);
    appendCursorFilter(jpql, params, q);

    appendOrderBy(jpql, q.getSort());

    return new QuerySpec(jpql.toString(), params);
  }

  private boolean hasTags(ProblemAttemptQuery q) {
    return q.getTagSlugs() != null && !q.getTagSlugs().isEmpty();
  }

  private void appendSelectFrom(StringBuilder jpql, boolean filterByTags) {
    jpql.append("select a ");
    jpql.append("  from AttemptEntity a ");
    jpql.append("  join a.problem p ");
    if (filterByTags) {
      jpql.append("  join p.terms t ");
      jpql.append("  join t.taxonomy tx ");
    }
  }

  private void appendLatestAttemptOnly(StringBuilder jpql) {
    jpql.append(" where a.attemptedAt = (");
    jpql.append("   select max(a2.attemptedAt) ");
    jpql.append("     from AttemptEntity a2 ");
    jpql.append("    where a2.problem = a.problem");
    jpql.append(" )");
  }

  private void appendQFilter(
      StringBuilder jpql, Map<String, Object> params, ProblemAttemptQuery q) {
    if (q.getQ() == null || q.getQ().isBlank()) {
      return;
    }
    jpql.append(
        " and ( p.siteProblemId like concat(:q, '%') "
            + " or lower(p.title) like concat('%', lower(:q), '%') )");
    params.put("q", q.getQ().trim());
  }

  private void appendSitesFilter(
      StringBuilder jpql, Map<String, Object> params, ProblemAttemptQuery q) {
    List<ProblemSite> sites = q.getSites();
    if (sites == null || sites.isEmpty()) {
      return;
    }
    jpql.append(" and p.site in :sites");
    params.put("sites", sites);
  }

  private void appendDifficultyFilter(
      StringBuilder jpql, Map<String, Object> params, ProblemAttemptQuery q) {
    if (q.getDifficultyMin() != null) {
      jpql.append(" and p.difficulty >= :dMin");
      params.put("dMin", q.getDifficultyMin());
    }
    if (q.getDifficultyMax() != null) {
      jpql.append(" and p.difficulty <= :dMax");
      params.put("dMax", q.getDifficultyMax());
    }
  }

  private void appendTagFilter(
      StringBuilder jpql, Map<String, Object> params, ProblemAttemptQuery q, boolean filterByTags) {
    if (!filterByTags) {
      return;
    }
    jpql.append(" and tx.code = :algoCode");
    jpql.append(" and t.slug in :tagSlugs");
    params.put("algoCode", ALGO_TAXONOMY_CODE);
    params.put("tagSlugs", q.getTagSlugs());
  }

  private void appendFinalVerdictFilter(
      StringBuilder jpql, Map<String, Object> params, ProblemAttemptQuery q) {
    AttemptVerdict finalVerdict = q.getFinalVerdict();
    if (finalVerdict == null) {
      return;
    }
    jpql.append(" and a.verdict = :finalVerdict");
    params.put("finalVerdict", finalVerdict);
  }

  private void appendAttemptedRangeFilter(
      StringBuilder jpql, Map<String, Object> params, ProblemAttemptQuery q) {
    if (q.getAttemptedFrom() != null) {
      jpql.append(" and a.attemptedAt >= :attemptedFrom");
      params.put("attemptedFrom", q.getAttemptedFrom());
    }
    if (q.getAttemptedTo() != null) {
      jpql.append(" and a.attemptedAt <= :attemptedTo");
      params.put("attemptedTo", q.getAttemptedTo());
    }
  }

  private void appendCursorFilter(
      StringBuilder jpql, Map<String, Object> params, ProblemAttemptQuery q) {
    Long cursorProblemId = q.getCursorProblemId();
    if (cursorProblemId == null) {
      return;
    }

    switch (q.getSort()) {
      case RECENT_ATTEMPT -> {
        jpql.append(
            " and ( a.attemptedAt < :cursorAttemptedAt"
                + " or (a.attemptedAt = :cursorAttemptedAt and p.id < :cursorProblemId) )");
        params.put("cursorAttemptedAt", q.getCursorAttemptedAt());
        params.put("cursorProblemId", cursorProblemId);
      }
      case OLDEST_ATTEMPT -> {
        jpql.append(
            " and ( a.attemptedAt > :cursorAttemptedAt"
                + " or (a.attemptedAt = :cursorAttemptedAt and p.id > :cursorProblemId) )");
        params.put("cursorAttemptedAt", q.getCursorAttemptedAt());
        params.put("cursorProblemId", cursorProblemId);
      }
      case HIGHEST_DIFFICULTY -> {
        jpql.append(
            " and ( p.difficulty < :cursorDifficulty"
                + " or (p.difficulty = :cursorDifficulty and p.id < :cursorProblemId) )");
        params.put("cursorDifficulty", q.getCursorDifficulty());
        params.put("cursorProblemId", cursorProblemId);
      }
      case LOWEST_DIFFICULTY -> {
        jpql.append(
            " and ( p.difficulty > :cursorDifficulty"
                + " or (p.difficulty = :cursorDifficulty and p.id > :cursorProblemId) )");
        params.put("cursorDifficulty", q.getCursorDifficulty());
        params.put("cursorProblemId", cursorProblemId);
      }
    }
  }

  private void appendOrderBy(StringBuilder jpql, ProblemSortOption sort) {
    jpql.append(" order by ");
    switch (sort) {
      case RECENT_ATTEMPT -> jpql.append(" a.attemptedAt desc, p.id desc");
      case OLDEST_ATTEMPT -> jpql.append(" a.attemptedAt asc, p.id asc");
      case HIGHEST_DIFFICULTY -> jpql.append(" p.difficulty desc, p.id desc");
      case LOWEST_DIFFICULTY -> jpql.append(" p.difficulty asc, p.id asc");
    }
  }

  private List<AttemptEntity> execute(QuerySpec spec, int limit) {
    TypedQuery<AttemptEntity> query = em.createQuery(spec.jpql(), AttemptEntity.class);
    spec.params().forEach(query::setParameter);
    query.setMaxResults(limit);
    return query.getResultList();
  }

  private Slice slice(List<AttemptEntity> fetched, int pageSize) {
    boolean hasNext = fetched.size() > pageSize;
    List<AttemptEntity> items = hasNext ? fetched.subList(0, pageSize) : fetched;
    return new Slice(items, hasNext);
  }

  private ProblemCandidatePage toPage(Slice slice, int pageSize) {
    if (slice.items().isEmpty()) {
      return new ProblemCandidatePage(List.of(), pageSize, false, null);
    }

    List<ProblemCandidate> items = new ArrayList<>(slice.items().size());
    Long nextCursorProblemId = null;

    for (AttemptEntity a : slice.items()) {
      items.add(toCandidate(a));
      nextCursorProblemId = a.getProblem().getId();
    }

    return new ProblemCandidatePage(items, pageSize, slice.hasNext(), nextCursorProblemId);
  }

  private ProblemCandidate toCandidate(AttemptEntity a) {
    ProblemEntity p = a.getProblem();
    ProblemLastAttemptSummary lastAttempt =
        new ProblemLastAttemptSummary(a.getVerdict(), a.getAttemptedAt());

    return new ProblemCandidate(
        p.getSite(), p.getSiteProblemId(), p.getTitle(), p.getDifficulty(), lastAttempt);
  }

  private record QuerySpec(String jpql, Map<String, Object> params) {}

  private record Slice(List<AttemptEntity> items, boolean hasNext) {}
}
