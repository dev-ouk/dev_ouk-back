package com.devouk.devouk_back.domain.problem;

public interface ProblemAttemptQueryPort {

  ProblemCandidatePage search(ProblemAttemptQuery query);
}
