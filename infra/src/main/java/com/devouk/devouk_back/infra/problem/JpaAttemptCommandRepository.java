package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.common.exception.ProblemNotFoundException;
import com.devouk.devouk_back.domain.problem.AttemptCommandPort;
import com.devouk.devouk_back.domain.problem.AttemptCreateResult;
import com.devouk.devouk_back.domain.problem.CreateAttemptCommand;
import java.time.OffsetDateTime;
import java.util.UUID;

public class JpaAttemptCommandRepository implements AttemptCommandPort {

  private final AttemptJpaRepository attemptRepo;
  private final ProblemJpaRepository problemRepo;

  public JpaAttemptCommandRepository(
      AttemptJpaRepository attemptRepo, ProblemJpaRepository problemRepo) {
    this.attemptRepo = attemptRepo;
    this.problemRepo = problemRepo;
  }

  @Override
  public AttemptCreateResult create(CreateAttemptCommand command) {
    ProblemEntity problem =
        problemRepo
            .findBySiteAndSiteProblemId(command.getSite(), command.getSiteProblemId())
            .orElseThrow(
                () -> new ProblemNotFoundException(command.getSite(), command.getSiteProblemId()));

    UUID uuid = UUID.randomUUID();
    OffsetDateTime now = OffsetDateTime.now();

    AttemptEntity entity =
        new AttemptEntity(
            uuid,
            problem,
            command.getTimeSpent(),
            command.getLanguage(),
            command.getNotes(),
            command.getVerdict(),
            command.getCode(),
            command.getFailType(),
            command.getFailDetail(),
            command.getSolution(),
            command.getAttemptedAt(),
            command.getNextReviewAt(),
            now);

    AttemptEntity saved = attemptRepo.save(entity);

    return new AttemptCreateResult(
        saved.getAttemptUuid(), problem.getSite(), problem.getSiteProblemId());
  }
}
