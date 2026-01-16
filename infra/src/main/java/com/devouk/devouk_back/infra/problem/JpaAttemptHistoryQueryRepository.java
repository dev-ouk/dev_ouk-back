package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.common.exception.ProblemNotFoundException;
import com.devouk.devouk_back.domain.problem.AttemptHistoryItem;
import com.devouk.devouk_back.domain.problem.AttemptHistoryQueryPort;
import com.devouk.devouk_back.domain.problem.AttemptHistoryResult;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import java.util.List;

public class JpaAttemptHistoryQueryRepository implements AttemptHistoryQueryPort {

  private final AttemptJpaRepository attemptRepo;
  private final ProblemJpaRepository problemRepo;

  public JpaAttemptHistoryQueryRepository(
      AttemptJpaRepository attemptRepo, ProblemJpaRepository problemRepo) {
    this.attemptRepo = attemptRepo;
    this.problemRepo = problemRepo;
  }

  @Override
  public AttemptHistoryResult findAllByProblemKey(ProblemSite site, String siteProblemId) {
    ProblemEntity problem =
        problemRepo
            .findBySiteAndSiteProblemId(site, siteProblemId)
            .orElseThrow(() -> new ProblemNotFoundException(site, siteProblemId));

    long total = attemptRepo.countByProblem_Id(problem.getId());
    List<AttemptEntity> entities =
        attemptRepo.findByProblem_IdOrderByAttemptedAtDescIdDesc(problem.getId());

    List<AttemptHistoryItem> items =
        entities.stream()
            .map(
                a ->
                    new AttemptHistoryItem(
                        a.getAttemptUuid(),
                        a.getTimeSpent() != null ? a.getTimeSpent() : 0,
                        a.getLanguage(),
                        a.getNotes(),
                        a.getVerdict(),
                        a.getCode(),
                        a.getAttemptedAt(),
                        a.getFailType(),
                        a.getFailDetail(),
                        a.getSolution(),
                        a.getNextReviewAt()))
            .toList();

    return new AttemptHistoryResult(total, items);
  }
}
