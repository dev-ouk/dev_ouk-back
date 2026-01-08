package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.problem.AttemptFailType;
import com.devouk.devouk_back.domain.problem.AttemptLanguage;
import com.devouk.devouk_back.domain.problem.AttemptVerdict;
import com.devouk.devouk_back.infra.problem.convert.AttemptLanguageConverter;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "ct_attempt")
public class AttemptEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "attempt_id")
  private Long id;

  @Column(name = "attempt_uuid", nullable = false)
  private UUID attemptUuid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "problem_id", nullable = false)
  private ProblemEntity problem;

  @Column(name = "time_spent", nullable = false)
  private Integer timeSpent;

  @Convert(converter = AttemptLanguageConverter.class)
  @Column(name = "language", nullable = false, columnDefinition = "ct_language")
  private AttemptLanguage language;

  @Lob
  @Column(name = "notes")
  private String notes;

  @Enumerated(EnumType.STRING)
  @Column(name = "verdict", nullable = false, columnDefinition = "ct_verdict")
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private AttemptVerdict verdict;

  @Lob
  @Column(name = "code")
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(name = "fail_type", columnDefinition = "ct_fail_type")
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private AttemptFailType failType;

  @Lob
  @Column(name = "fail_detail")
  private String failDetail;

  @Lob
  @Column(name = "solution")
  private String solution;

  @Column(name = "attempted_at", nullable = false)
  private OffsetDateTime attemptedAt;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @Column(name = "next_review_at")
  private OffsetDateTime nextReviewAt;

  protected AttemptEntity() {}

  public AttemptEntity(
      UUID attemptUuid,
      ProblemEntity problem,
      Integer timeSpent,
      AttemptLanguage language,
      String notes,
      AttemptVerdict verdict,
      String code,
      AttemptFailType failType,
      String failDetail,
      String solution,
      OffsetDateTime attemptedAt,
      OffsetDateTime nextReviewAt,
      OffsetDateTime now) {
    this.attemptUuid = attemptUuid;
    this.problem = problem;
    this.timeSpent = timeSpent;
    this.language = language;
    this.notes = notes;
    this.verdict = verdict;
    this.code = code;
    this.failType = failType;
    this.failDetail = failDetail;
    this.solution = solution;
    this.attemptedAt = attemptedAt;
    this.nextReviewAt = nextReviewAt;

    this.createdAt = now;
    this.updatedAt = now;
  }

  public ProblemEntity getProblem() {
    return problem;
  }

  public UUID getAttemptUuid() {
    return attemptUuid;
  }

  public Long getId() {
    return id;
  }

  public AttemptVerdict getVerdict() {
    return verdict;
  }

  public OffsetDateTime getAttemptedAt() {
    return attemptedAt;
  }
}
