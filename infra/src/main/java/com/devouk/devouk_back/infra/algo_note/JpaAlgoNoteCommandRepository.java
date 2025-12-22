package com.devouk.devouk_back.infra.algo_note;

import com.devouk.devouk_back.domain.algo_note.AlgoNote;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteCommandPort;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteStatus;
import com.devouk.devouk_back.domain.algo_note.CreateAlgoNoteCommand;
import com.devouk.devouk_back.domain.common.exception.AlgoNoteTagNotFoundException;
import com.devouk.devouk_back.domain.common.exception.DuplicateAlgoNoteSlugException;
import com.devouk.devouk_back.domain.common.exception.TaxonomyNotFoundException;
import com.devouk.devouk_back.infra.taxonomy.TermEntity;
import com.devouk.devouk_back.infra.taxonomy.TermJpaRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaAlgoNoteCommandRepository implements AlgoNoteCommandPort {

  private static final String ALGO_TAXONOMY_CODE = "algo";

  private final AlgoNoteJpaRepository noteRepo;
  private final TermJpaRepository termRepo;

  public JpaAlgoNoteCommandRepository(AlgoNoteJpaRepository noteRepo, TermJpaRepository termRepo) {
    this.noteRepo = noteRepo;
    this.termRepo = termRepo;
  }

  @Override
  public AlgoNote create(CreateAlgoNoteCommand command) {
    assertSlugAvailable(command.getSlug());

    List<String> tagSlugs = safeTagSlugs(command.getTagSlugs());
    List<TermEntity> terms = loadAndValidateTerms(tagSlugs);

    AlgoNoteEntity entity = buildEntity(command);
    attachTerms(entity, terms);

    AlgoNoteEntity saved = noteRepo.save(entity);
    return toDomain(saved, tagSlugs);
  }

  private void assertSlugAvailable(String slug) {
    if (noteRepo.existsBySlug(slug)) {
      throw new DuplicateAlgoNoteSlugException(slug);
    }
  }

  private List<String> safeTagSlugs(List<String> tagSlugs) {
    return Optional.ofNullable(tagSlugs).orElse(List.of());
  }

  private List<TermEntity> loadAndValidateTerms(List<String> tagSlugs) {
    if (tagSlugs.isEmpty()) {
      return List.of();
    }

    ensureAlgoTaxonomyExists();

    List<TermEntity> terms = termRepo.findByTaxonomy_CodeAndSlugIn(ALGO_TAXONOMY_CODE, tagSlugs);
    assertAllTagsFound(tagSlugs, terms);

    return terms;
  }

  private void ensureAlgoTaxonomyExists() {
    if (!termRepo.existsByTaxonomy_Code(ALGO_TAXONOMY_CODE)) {
      throw new TaxonomyNotFoundException(ALGO_TAXONOMY_CODE);
    }
  }

  private void assertAllTagsFound(List<String> requestedSlugs, List<TermEntity> foundTerms) {
    Set<String> foundSlugs =
        foundTerms.stream().map(TermEntity::getSlug).collect(Collectors.toSet());

    List<String> missing = requestedSlugs.stream().filter(s -> !foundSlugs.contains(s)).toList();
    if (!missing.isEmpty()) {
      throw new AlgoNoteTagNotFoundException(missing);
    }
  }

  private AlgoNoteEntity buildEntity(CreateAlgoNoteCommand command) {
    OffsetDateTime publishedAt = computePublishedAt(command.getStatus());

    return new AlgoNoteEntity(
        command.getTitle(),
        command.getSlug(),
        command.getContentJson(),
        command.getContentHtml(),
        command.getContentText(),
        command.getStatus(),
        command.isPublic(),
        command.isPin(),
        publishedAt);
  }

  private OffsetDateTime computePublishedAt(AlgoNoteStatus status) {
    return (status == AlgoNoteStatus.PUBLISHED) ? OffsetDateTime.now() : null;
  }

  private void attachTerms(AlgoNoteEntity entity, List<TermEntity> terms) {
    for (TermEntity t : terms) {
      entity.addTerm(t);
    }
  }

  private AlgoNote toDomain(AlgoNoteEntity saved, List<String> tagSlugs) {
    return new AlgoNote(
        saved.getId(),
        saved.getTitle(),
        saved.getSlug(),
        saved.getContentJson(),
        saved.getContentHtml(),
        saved.getContentText(),
        saved.getStatus(),
        saved.isPublic(),
        saved.isPin(),
        tagSlugs);
  }
}
