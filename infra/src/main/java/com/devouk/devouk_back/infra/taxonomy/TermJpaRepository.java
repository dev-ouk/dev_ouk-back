package com.devouk.devouk_back.infra.taxonomy;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TermJpaRepository extends JpaRepository<TermEntity, Long> {

  @Query(
      """
        select t
            from TermEntity t
                join t.taxonomy x
                    where x.code = :code
                        and ( :q is null or :q = ''
                            or lower(t.name) like concat('%', lower(:q), '%')
                                or lower(t.slug) like concat('%', lower(:q), '%'))
                                    order by t.name asc
    """)
  List<TermEntity> searchByTaxonomyAndQ(@Param("code") String code, @Param("q") String q);

  boolean existsByTaxonomy_Code(String code);
}
