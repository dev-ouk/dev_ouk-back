package com.devouk.devouk_back.domain.problem;

import java.util.Optional;

public interface ProblemDetailQueryPort {
  Optional<ProblemDetail> findByKey(ProblemSite site, String siteProblemId);
}
