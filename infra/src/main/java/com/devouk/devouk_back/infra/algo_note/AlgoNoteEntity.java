package com.devouk.devouk_back.infra.algo_note;

import jakarta.persistence.*;

@Entity
@Table(name = "algo_note")
public class AlgoNoteEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "algo_note_id")
  private Long id;

  @Column(name = "slug", nullable = false, length = 200, unique = true)
  private String slug;

  protected AlgoNoteEntity() {}

  public Long getId() {
    return id;
  }

  public String getSlug() {
    return slug;
  }
}
