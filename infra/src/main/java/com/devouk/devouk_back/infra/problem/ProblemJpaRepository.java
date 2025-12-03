package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.problem.ProblemSite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemJpaRepository extends JpaRepository<ProblemEntity, Long> {
  boolean existsBySiteAndSiteProblemId(ProblemSite site, String siteProblemId);
}
