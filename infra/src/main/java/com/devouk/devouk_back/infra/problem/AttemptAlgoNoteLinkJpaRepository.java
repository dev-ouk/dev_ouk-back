package com.devouk.devouk_back.infra.problem;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttemptAlgoNoteLinkJpaRepository
    extends JpaRepository<AttemptAlgoNoteLinkEntity, AttemptAlgoNoteLinkEntity.Pk> {

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  void deleteByAttempt_Id(Long attemptId);

  List<AttemptAlgoNoteLinkEntity> findByAttempt_Id(Long attemptId);

  @Query(
      """
        select l
            from AttemptAlgoNoteLinkEntity l
                join fetch l.algoNote n
                    where l.attempt.id = :attemptId
                        order by n.createdAt desc, n.id desc
    """)
  List<AttemptAlgoNoteLinkEntity> findByAttemptIdWithAlgoNote(@Param("attemptId") Long attemptId);
}
