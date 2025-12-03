package com.devouk.devouk_back.domain.problem;

public interface ProblemCommandPort {

  Problem create(CreateProblemCommand command);
}
