package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.common.exception.AlgoNoteSlugsNotFoundException;
import com.devouk.devouk_back.domain.common.exception.AttemptNotFoundException;
import com.devouk.devouk_back.domain.problem.AttemptAlgoNoteCommandPort;
import com.devouk.devouk_back.domain.problem.ReplaceAttemptAlgoNotesCommand;
import com.devouk.devouk_back.infra.algo_note.AlgoNoteEntity;
import com.devouk.devouk_back.infra.algo_note.AlgoNoteJpaRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaAttemptAlgoNoteCommandRepository implements AttemptAlgoNoteCommandPort {

  private final AttemptJpaRepository attemptRepo;
  private final AlgoNoteJpaRepository algoNoteRepo;
  private final AttemptAlgoNoteLinkJpaRepository linkRepo;

  public JpaAttemptAlgoNoteCommandRepository(
      AttemptJpaRepository attemptRepo,
      AlgoNoteJpaRepository algoNoteRepo,
      AttemptAlgoNoteLinkJpaRepository linkRepo) {
    this.attemptRepo = attemptRepo;
    this.algoNoteRepo = algoNoteRepo;
    this.linkRepo = linkRepo;
  }

  @Override
  public void replace(ReplaceAttemptAlgoNotesCommand command) {
    AttemptEntity attempt =
        attemptRepo
            .findByAttemptUuid(command.getAttemptUuid())
            .orElseThrow(() -> new AttemptNotFoundException(command.getAttemptUuid()));

    List<String> requestedSlugs = safeList(command.getAlgoNoteSlugs());

    linkRepo.deleteByAttempt_Id(attempt.getId());

    if (requestedSlugs.isEmpty()) {
      return;
    }

    List<AlgoNoteEntity> notes = algoNoteRepo.findBySlugIn(requestedSlugs);
    assertAllNotesFound(requestedSlugs, notes);

    List<AttemptAlgoNoteLinkEntity> links =
        notes.stream().map(n -> new AttemptAlgoNoteLinkEntity(attempt, n)).toList();

    linkRepo.saveAll(links);
  }

  private List<String> safeList(List<String> v) {
    return v == null ? List.of() : v;
  }

  private void assertAllNotesFound(List<String> requestedSlugs, List<AlgoNoteEntity> found) {
    Set<String> foundSlugs =
        found.stream().map(AlgoNoteEntity::getSlug).collect(Collectors.toSet());

    List<String> missing = requestedSlugs.stream().filter(s -> !foundSlugs.contains(s)).toList();

    if (!missing.isEmpty()) {
      throw new AlgoNoteSlugsNotFoundException(missing);
    }
  }
}
