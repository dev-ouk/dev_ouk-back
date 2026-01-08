package com.devouk.devouk_back.domain.problem;

public interface AttemptCommandPort {
  AttemptCreateResult create(CreateAttemptCommand command);
}
