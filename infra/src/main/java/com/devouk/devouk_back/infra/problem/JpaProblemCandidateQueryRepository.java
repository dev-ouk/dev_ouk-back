package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.problem.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class JpaProblemCandidateQueryRepository implements ProblemCandidateQueryPort {

  private final ProblemJpaRepository problemRepo;
  private final AttemptJpaRepository attemptRepo;

  public JpaProblemCandidateQueryRepository(
      ProblemJpaRepository problemRepo, AttemptJpaRepository attemptRepo) {
    this.problemRepo = problemRepo;
    this.attemptRepo = attemptRepo;
  }

  @Override
  public ProblemCandidatePage search(ProblemCandidateQuery query) {
    int requestedSize = query.getSize();
    int pageSize = Math.max(1, Math.min(requestedSize, 100));

    Pageable pageable = PageRequest.of(0, pageSize + 1);

    List<ProblemEntity> problems;
    if (query.getSites() == null || query.getSites().isEmpty()) {
      problems =
          problemRepo.searchCandidatesNoSite(
              normalizeQ(query.getQ()), query.getCursorProblemId(), pageable);
    } else {
      problems =
          problemRepo.searchCandidatesWithSites(
              normalizeQ(query.getQ()), query.getSites(), query.getCursorProblemId(), pageable);
    }

    boolean hasNext = problems.size() > pageSize;
    if (hasNext) {
      problems = problems.subList(0, pageSize);
    }

    if (problems.isEmpty()) {
      return new ProblemCandidatePage(List.of(), pageSize, false, null);
    }

    List<Long> problemIds = problems.stream().map(ProblemEntity::getId).toList();
    Map<Long, ProblemLastAttemptSummary> lastAttemptMap = loadLastAttempts(problemIds);

    List<ProblemCandidate> items =
        problems.stream().map(p -> toDomainCandidate(p, lastAttemptMap.get(p.getId()))).toList();

    Long nextCursorId = hasNext ? problems.get(problems.size() - 1).getId() : null;

    return new ProblemCandidatePage(items, pageSize, hasNext, nextCursorId);
  }

  private String normalizeQ(String q) {
    if (q == null) {
      return null;
    }
    String trimmed = q.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private Map<Long, ProblemLastAttemptSummary> loadLastAttempts(List<Long> problemIds) {
    if (problemIds.isEmpty()) {
      return Map.of();
    }

    List<AttemptEntity> attempts = attemptRepo.findLatestByProblemIds(problemIds);
    Map<Long, ProblemLastAttemptSummary> result = new HashMap<>();

    for (AttemptEntity a : attempts) {
      Long problemId = a.getProblem().getId();
      ProblemLastAttemptSummary existing = result.get(problemId);
      if (existing == null || existing.getAttemptedAt().isBefore(a.getAttemptedAt())) {
        result.put(problemId, new ProblemLastAttemptSummary(a.getVerdict(), a.getAttemptedAt()));
      }
    }

    return result;
  }

  private ProblemCandidate toDomainCandidate(
      ProblemEntity p, ProblemLastAttemptSummary lastAttempt) {
    return new ProblemCandidate(
        p.getSite(), p.getSiteProblemId(), p.getTitle(), p.getDifficulty(), lastAttempt);
  }
}
