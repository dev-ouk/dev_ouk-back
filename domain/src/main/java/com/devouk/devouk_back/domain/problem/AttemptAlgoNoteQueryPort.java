package com.devouk.devouk_back.domain.problem;

import java.util.UUID;

public interface AttemptAlgoNoteQueryPort {
  AttemptAlgoNotesResult findAllByAttemptUuid(UUID attemptUuid);
}
