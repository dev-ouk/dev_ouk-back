package com.devouk.devouk_back.infra.algo_note;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlgoNoteJpaRepository extends JpaRepository<AlgoNoteEntity, Long> {
  boolean existsBySlug(String slug);

  @Query(
      """
            select distinct n
                    from AlgoNoteEntity n
                            left join fetch n.terms t
                            left join fetch t.taxonomy tx
                    where n.slug = :slug
        """)
  Optional<AlgoNoteEntity> findOneBySlugWithTerms(@Param("slug") String slug);

  List<AlgoNoteEntity> findBySlugIn(Collection<String> slugs);
}
