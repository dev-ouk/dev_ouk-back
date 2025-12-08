package com.devouk.devouk_back.domain.problem;

public interface ProblemCandidateQueryPort {

  ProblemCandidatePage search(ProblemCandidateQuery query);
}
