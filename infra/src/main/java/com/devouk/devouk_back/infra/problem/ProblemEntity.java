package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.problem.ProblemSite;
import com.devouk.devouk_back.infra.taxonomy.TermEntity;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(
    name = "ct_problem",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uq_ct_problem_site_sid",
          columnNames = {"site", "site_problem_id"})
    })
public class ProblemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "problem_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "site", nullable = false, columnDefinition = "ct_site")
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private ProblemSite site;

  @Column(name = "site_problem_id", nullable = false, length = 64)
  private String siteProblemId;

  @Column(name = "title", nullable = false, length = 256)
  private String title;

  @Column(name = "url", nullable = false, length = 512)
  private String url;

  @Column(name = "difficulty")
  private Integer difficulty;

  @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
  private OffsetDateTime updatedAt;

  @ManyToMany
  @JoinTable(
      name = "ct_problem_term",
      joinColumns = @JoinColumn(name = "problem_id"),
      inverseJoinColumns = @JoinColumn(name = "term_id"))
  private Set<TermEntity> terms = new HashSet<>();

  protected ProblemEntity() {}

  public ProblemEntity(
      ProblemSite site, String siteProblemId, String title, String url, Integer difficulty) {
    this.site = site;
    this.siteProblemId = siteProblemId;
    this.title = title;
    this.url = url;
    this.difficulty = difficulty;
  }

  public Long getId() {
    return id;
  }

  public ProblemSite getSite() {
    return site;
  }

  public String getSiteProblemId() {
    return siteProblemId;
  }

  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }

  public Integer getDifficulty() {
    return difficulty;
  }

  public Set<TermEntity> getTerms() {
    return terms;
  }

  public void addTerm(TermEntity term) {
    this.terms.add(term);
  }
}
