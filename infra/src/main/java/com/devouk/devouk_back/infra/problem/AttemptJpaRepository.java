package com.devouk.devouk_back.infra.problem;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttemptJpaRepository extends JpaRepository<AttemptEntity, Long> {

  @Query(
      """
            select a
                        from AttemptEntity a
                                    where a.problem.id in :problemIds
                                                and a.attemptedAt = (
                                                            select max(a2.attemptedAt)
                                                                        from AttemptEntity a2
                                                                                    where a2.problem = a.problem
                                                            )
            """)
  List<AttemptEntity> findLatestByProblemIds(@Param("problemIds") Collection<Long> problemIds);

  boolean existsByAttemptUuid(UUID attemptUuid);

  List<AttemptEntity> findByProblem_IdOrderByAttemptedAtDescIdDesc(Long problemId);

  long countByProblem_Id(Long problemId);
}
