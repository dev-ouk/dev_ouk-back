package com.devouk.devouk_back.infra.algo_note;

import com.devouk.devouk_back.domain.algo_note.AlgoNoteStatus;
import com.devouk.devouk_back.infra.taxonomy.TermEntity;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "algo_note")
public class AlgoNoteEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "algo_note_id")
  private Long id;

  @Column(name = "title", nullable = false, length = 200)
  private String title;

  @Column(name = "slug", nullable = false, length = 200, unique = true)
  private String slug;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "content_json", nullable = false, columnDefinition = "jsonb")
  private String contentJson;

  @Lob
  @Column(name = "content_html")
  private String contentHtml;

  @Lob
  @Column(name = "content_text")
  private String contentText;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, columnDefinition = "algo_note_status")
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private AlgoNoteStatus status;

  @Column(name = "is_pin", nullable = false)
  private boolean isPin;

  @Column(name = "is_public", nullable = false)
  private boolean isPublic;

  @Column(name = "published_at")
  private OffsetDateTime publishedAt;

  @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
  private OffsetDateTime updatedAt;

  @ManyToMany
  @JoinTable(
      name = "ct_algo_note_term",
      joinColumns = @JoinColumn(name = "algo_note_id"),
      inverseJoinColumns = @JoinColumn(name = "term_id"))
  private Set<TermEntity> terms = new HashSet<>();

  public AlgoNoteEntity(
      String title,
      String slug,
      String contentJson,
      String contentHtml,
      String contentText,
      AlgoNoteStatus status,
      boolean isPublic,
      boolean isPin,
      OffsetDateTime publishedAt) {
    this.title = title;
    this.slug = slug;
    this.contentJson = contentJson;
    this.contentHtml = contentHtml;
    this.contentText = contentText;
    this.status = status;
    this.isPublic = isPublic;
    this.isPin = isPin;
    this.publishedAt = publishedAt;
  }

  protected AlgoNoteEntity() {}

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getSlug() {
    return slug;
  }

  public String getContentJson() {
    return contentJson;
  }

  public String getContentHtml() {
    return contentHtml;
  }

  public String getContentText() {
    return contentText;
  }

  public AlgoNoteStatus getStatus() {
    return status;
  }

  public boolean isPublic() {
    return isPublic;
  }

  public boolean isPin() {
    return isPin;
  }

  public Set<TermEntity> getTerms() {
    return terms;
  }

  public void addTerm(TermEntity term) {
    this.terms.add(term);
  }
}
