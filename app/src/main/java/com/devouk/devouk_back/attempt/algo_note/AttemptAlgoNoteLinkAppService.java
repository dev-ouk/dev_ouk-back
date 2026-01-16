package com.devouk.devouk_back.attempt.algo_note;

import com.devouk.devouk_back.attempt.algo_note.dto.AttemptAlgoNotesReplaceRequest;
import com.devouk.devouk_back.domain.problem.AttemptAlgoNoteCommandPort;
import com.devouk.devouk_back.domain.problem.ReplaceAttemptAlgoNotesCommand;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttemptAlgoNoteLinkAppService {

  private final AttemptAlgoNoteCommandPort commandPort;

  public AttemptAlgoNoteLinkAppService(AttemptAlgoNoteCommandPort commandPort) {
    this.commandPort = commandPort;
  }

  public void replace(UUID attemptUuid, AttemptAlgoNotesReplaceRequest req) {
    List<String> normalized = normalizeSlugs(req.getAlgoNoteSlugs());

    ReplaceAttemptAlgoNotesCommand cmd =
        new ReplaceAttemptAlgoNotesCommand(attemptUuid, normalized);

    commandPort.replace(cmd);
  }

  private List<String> normalizeSlugs(List<String> slugs) {
    if (slugs == null || slugs.isEmpty()) {
      return List.of();
    }

    LinkedHashSet<String> set =
        slugs.stream()
            .map(s -> s == null ? null : s.trim())
            .filter(s -> s != null && !s.isEmpty())
            .collect(Collectors.toCollection(LinkedHashSet::new));

    return List.copyOf(set);
  }
}
