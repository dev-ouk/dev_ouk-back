package com.devouk.devouk_back.infra.problem;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface AttemptAlgoNoteLinkJpaRepository
    extends JpaRepository<AttemptAlgoNoteLinkEntity, AttemptAlgoNoteLinkEntity.Pk> {

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  void deleteByAttempt_Id(Long attemptId);

  List<AttemptAlgoNoteLinkEntity> findByAttempt_Id(Long attemptId);
}
