package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.common.exception.AttemptNotFoundException;
import com.devouk.devouk_back.domain.problem.AttemptAlgoNoteItem;
import com.devouk.devouk_back.domain.problem.AttemptAlgoNoteQueryPort;
import com.devouk.devouk_back.domain.problem.AttemptAlgoNotesResult;
import java.util.List;
import java.util.UUID;

public class JpaAttemptAlgoNoteQueryRepository implements AttemptAlgoNoteQueryPort {

  private final AttemptJpaRepository attemptRepo;
  private final AttemptAlgoNoteLinkJpaRepository linkRepo;

  public JpaAttemptAlgoNoteQueryRepository(
      AttemptJpaRepository attemptRepo, AttemptAlgoNoteLinkJpaRepository linkRepo) {
    this.attemptRepo = attemptRepo;
    this.linkRepo = linkRepo;
  }

  @Override
  public AttemptAlgoNotesResult findAllByAttemptUuid(UUID attemptUuid) {
    AttemptEntity attempt =
        attemptRepo
            .findByAttemptUuid(attemptUuid)
            .orElseThrow(() -> new AttemptNotFoundException(attemptUuid));

    List<AttemptAlgoNoteLinkEntity> links = linkRepo.findByAttemptIdWithAlgoNote(attempt.getId());

    List<AttemptAlgoNoteItem> items =
        links.stream()
            .map(AttemptAlgoNoteLinkEntity::getAlgoNote)
            .filter(n -> n != null)
            .map(n -> new AttemptAlgoNoteItem(n.getSlug(), n.getTitle(), n.getCreatedAt()))
            .toList();

    return AttemptAlgoNotesResult.of(items);
  }
}
