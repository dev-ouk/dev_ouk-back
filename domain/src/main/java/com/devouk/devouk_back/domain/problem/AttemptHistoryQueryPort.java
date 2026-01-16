package com.devouk.devouk_back.domain.problem;

public interface AttemptHistoryQueryPort {
  AttemptHistoryResult findAllByProblemKey(ProblemSite site, String siteProblemId);
}
