package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.problem.*;
import com.devouk.devouk_back.domain.taxonomy.Term;
import com.devouk.devouk_back.infra.taxonomy.TermEntity;
import jakarta.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

public class JpaProblemDetailQueryRepository implements ProblemDetailQueryPort {

  private final EntityManager em;
  private final AttemptJpaRepository attemptRepo;

  public JpaProblemDetailQueryRepository(EntityManager em, AttemptJpaRepository attemptRepo) {
    this.em = em;
    this.attemptRepo = attemptRepo;
  }

  @Override
  public Optional<ProblemDetail> findByKey(ProblemSite site, String siteProblemId) {
    if (site == null || siteProblemId == null || siteProblemId.isBlank()) {
      return Optional.empty();
    }

    List<ProblemEntity> rows =
        em.createQuery(
                """
                        select distinct p
                            from ProblemEntity p
                            left join fetch p.terms t
                            left join fetch t.taxonomy tx
                            where p.site = :site
                            and p.siteProblemId = :sid
                        """,
                ProblemEntity.class)
            .setParameter("site", site)
            .setParameter("sid", siteProblemId)
            .getResultList();

    if (rows.isEmpty()) {
      return Optional.empty();
    }

    ProblemEntity p = rows.get(0);

    ProblemLastAttemptSummary lastAttempt = findLastAttemptSummary(p.getId());
    Map<String, List<Term>> taxonomies = groupByTaxonomy(p.getTerms());

    return Optional.of(
        new ProblemDetail(
            p.getSite(),
            p.getSiteProblemId(),
            p.getTitle(),
            p.getDifficulty(),
            p.getUrl(),
            lastAttempt,
            taxonomies));
  }

  private ProblemLastAttemptSummary findLastAttemptSummary(Long problemId) {
    List<AttemptEntity> latest = attemptRepo.findLatestByProblemIds(List.of(problemId));
    if (latest == null || latest.isEmpty()) {
      return null;
    }

    AttemptEntity a = latest.get(0);

    return new ProblemLastAttemptSummary(toAttemptVerdict(a.getVerdict()), a.getAttemptedAt());
  }

  private AttemptVerdict toAttemptVerdict(Object raw) {
    if (raw == null) {
      return null;
    }
    if (raw instanceof AttemptVerdict v) {
      return v;
    }
    return AttemptVerdict.valueOf(raw.toString());
  }

  private Map<String, List<Term>> groupByTaxonomy(Set<TermEntity> terms) {
    if (terms == null || terms.isEmpty()) {
      return Map.of();
    }

    Map<String, List<Term>> grouped =
        terms.stream()
            .filter(t -> t.getTaxonomy() != null && t.getTaxonomy().getCode() != null)
            .collect(
                Collectors.groupingBy(
                    t -> t.getTaxonomy().getCode(),
                    Collectors.mapping(
                        t -> new Term(t.getSlug(), t.getName()), Collectors.toList())));

    grouped.values().forEach(list -> list.sort(Comparator.comparing(Term::getName)));

    return grouped;
  }
}
