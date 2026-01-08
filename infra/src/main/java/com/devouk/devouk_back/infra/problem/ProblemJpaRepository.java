package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.problem.ProblemSite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProblemJpaRepository extends JpaRepository<ProblemEntity, Long> {
  boolean existsBySiteAndSiteProblemId(ProblemSite site, String siteProblemId);

  @Query(
      """
          select p
                    from ProblemEntity p
                              where(:q is null or :q = ''
                                        or p.siteProblemId like concat(:q, '%')
                                                  or lower(p.title) like concat('%', lower(:q), '%'))
                                                            and (:cursorId is null or p.id > :cursorId)
                                                                      order by p.id asc
          """)
  List<ProblemEntity> searchCandidatesNoSite(
      @Param("q") String q, @Param("cursorId") Long cursorId, Pageable pageable);

  @Query(
      """
          select p
                    from ProblemEntity p
                              where p.site in :sites
                                        and (:q is null or :q = ''
                                                  or p.siteProblemId like concat(:q, '%')
                                                            or lower(p.title) like concat('%', lower(:q), '%'))
                                                                      and (:cursorId is null or p.id > :cursorId)
                                                                                order by p.id asc
          """)
  List<ProblemEntity> searchCandidatesWithSites(
      @Param("q") String q,
      @Param("sites") List<ProblemSite> sites,
      @Param("cursorId") Long cursorId,
      Pageable pageable);

  Optional<ProblemEntity> findBySiteAndSiteProblemId(ProblemSite site, String siteProblemId);
}
