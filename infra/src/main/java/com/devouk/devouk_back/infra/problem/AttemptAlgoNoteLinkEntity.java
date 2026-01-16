package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.infra.algo_note.AlgoNoteEntity;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ct_attempt_algo_note")
public class AttemptAlgoNoteLinkEntity {

  @EmbeddedId private Pk pk;

  @MapsId("attemptId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attempt_id", nullable = false)
  private AttemptEntity attempt;

  @MapsId("algoNoteId")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "algo_note_id", nullable = false)
  private AlgoNoteEntity algoNote;

  protected AttemptAlgoNoteLinkEntity() {}

  public AttemptAlgoNoteLinkEntity(AttemptEntity attempt, AlgoNoteEntity algoNote) {
    this.attempt = attempt;
    this.algoNote = algoNote;
    this.pk = new Pk(attempt.getId(), algoNote.getId());
  }

  @Embeddable
  public static class Pk implements Serializable {

    @Column(name = "attempt_id")
    private Long attemptId;

    @Column(name = "algo_note_id")
    private Long algoNoteId;

    protected Pk() {}

    public Pk(Long attemptId, Long algoNoteId) {
      this.attemptId = attemptId;
      this.algoNoteId = algoNoteId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Pk pk1)) {
        return false;
      }
      return Objects.equals(attemptId, pk1.attemptId) && Objects.equals(algoNoteId, pk1.algoNoteId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(attemptId, algoNoteId);
    }
  }
}
