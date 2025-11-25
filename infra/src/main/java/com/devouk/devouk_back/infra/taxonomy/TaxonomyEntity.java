package com.devouk.devouk_back.infra.taxonomy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "taxonomy")
public class TaxonomyEntity {
  @Id
  @Column(name = "taxonomy_id")
  private Long id;

  @Column(unique = true, nullable = false, length = 32)
  private String code;

  @Column(nullable = false, length = 64)
  private String name;

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }
}
