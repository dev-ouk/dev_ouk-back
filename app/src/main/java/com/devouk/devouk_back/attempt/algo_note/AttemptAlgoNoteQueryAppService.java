package com.devouk.devouk_back.attempt.algo_note;

import com.devouk.devouk_back.attempt.algo_note.dto.AttemptAlgoNotesGetResponse;
import com.devouk.devouk_back.domain.problem.AttemptAlgoNoteQueryPort;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AttemptAlgoNoteQueryAppService {
  private final AttemptAlgoNoteQueryPort queryPort;

  public AttemptAlgoNoteQueryAppService(AttemptAlgoNoteQueryPort queryPort) {
    this.queryPort = queryPort;
  }

  public AttemptAlgoNotesGetResponse getAll(UUID attemptUuid) {
    return AttemptAlgoNotesGetResponse.from(queryPort.findAllByAttemptUuid(attemptUuid));
  }
}
