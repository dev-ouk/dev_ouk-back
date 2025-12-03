package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.common.exception.DuplicateProblemException;
import com.devouk.devouk_back.domain.common.exception.ProblemTagNotFoundException;
import com.devouk.devouk_back.domain.common.exception.TaxonomyNotFoundException;
import com.devouk.devouk_back.domain.problem.CreateProblemCommand;
import com.devouk.devouk_back.domain.problem.Problem;
import com.devouk.devouk_back.domain.problem.ProblemCommandPort;
import com.devouk.devouk_back.infra.taxonomy.TermEntity;
import com.devouk.devouk_back.infra.taxonomy.TermJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaProblemCommandRepository implements ProblemCommandPort {

  private static final String ALGO_TAXONOMY_CODE = "algo";

  private final ProblemJpaRepository problemRepo;
  private final TermJpaRepository termRepo;

  public JpaProblemCommandRepository(ProblemJpaRepository problemRepo, TermJpaRepository termRepo) {
    this.problemRepo = problemRepo;
    this.termRepo = termRepo;
  }

  @Override
  public Problem create(CreateProblemCommand command) {
    if (problemRepo.existsBySiteAndSiteProblemId(command.getSite(), command.getSiteProblemId())) {
      throw new DuplicateProblemException(command.getSite(), command.getSiteProblemId());
    }

    List<String> tagSlugs = Optional.ofNullable(command.getTagSlugs()).orElse(List.of());
    List<TermEntity> terms = List.of();

    if (!tagSlugs.isEmpty()) {
      if (!termRepo.existsByTaxonomy_Code(ALGO_TAXONOMY_CODE)) {
        throw new TaxonomyNotFoundException(ALGO_TAXONOMY_CODE);
      }

      terms = termRepo.findByTaxonomy_CodeAndSlugIn(ALGO_TAXONOMY_CODE, tagSlugs);

      Set<String> foundSlugs = terms.stream().map(TermEntity::getSlug).collect(Collectors.toSet());

      List<String> missing = tagSlugs.stream().filter(slug -> !foundSlugs.contains(slug)).toList();

      if (!missing.isEmpty()) {
        throw new ProblemTagNotFoundException(missing);
      }
    }

    ProblemEntity entity =
        new ProblemEntity(
            command.getSite(),
            command.getSiteProblemId(),
            command.getTitle(),
            command.getUrl(),
            command.getDifficulty());

    for (TermEntity term : terms) {
      entity.addTerm(term);
    }

    ProblemEntity saved = problemRepo.save(entity);

    return new Problem(
        saved.getId(),
        saved.getSite(),
        saved.getSiteProblemId(),
        saved.getTitle(),
        saved.getUrl(),
        saved.getDifficulty(),
        tagSlugs);
  }
}
