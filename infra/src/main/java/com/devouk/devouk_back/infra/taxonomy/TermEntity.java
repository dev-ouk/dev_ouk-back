package com.devouk.devouk_back.infra.taxonomy;

import jakarta.persistence.*;

@Entity
@Table(name = "term")
public class TermEntity {
  @Id
  @Column(name = "term_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "taxonomy_id", nullable = false)
  private TaxonomyEntity taxonomy;

  @Column(nullable = false, length = 64)
  private String slug;

  @Column(nullable = false, length = 64)
  private String name;

  public String getSlug() {
    return slug;
  }

  public String getName() {
    return name;
  }

  public TaxonomyEntity getTaxonomy() {
    return taxonomy;
  }
}
