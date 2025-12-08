package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.problem.AttemptVerdict;
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

  @Column(name = "attempted_at", nullable = false)
  private OffsetDateTime attemptedAt;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  @Column(name = "next_review_at")
  private OffsetDateTime nextReviewAt;

  protected AttemptEntity() {}

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
