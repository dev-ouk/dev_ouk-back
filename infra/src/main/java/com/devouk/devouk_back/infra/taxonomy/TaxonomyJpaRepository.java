package com.devouk.devouk_back.infra.taxonomy;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxonomyJpaRepository extends JpaRepository<TaxonomyEntity, Long> {
  Optional<TaxonomyEntity> findByCode(String code);
}
