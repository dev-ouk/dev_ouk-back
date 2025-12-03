package com.devouk.devouk_back.problem.service;

import com.devouk.devouk_back.domain.common.exception.InvalidProblemSiteException;
import com.devouk.devouk_back.domain.problem.CreateProblemCommand;
import com.devouk.devouk_back.domain.problem.Problem;
import com.devouk.devouk_back.domain.problem.ProblemCommandPort;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import com.devouk.devouk_back.problem.dto.ProblemCreateRequest;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProblemAppService {

  private final ProblemCommandPort problemCommandPort;

  public ProblemAppService(ProblemCommandPort problemCommandPort) {
    this.problemCommandPort = problemCommandPort;
  }

  public Problem createProblem(ProblemCreateRequest request) {
    ProblemSite site;
    try {
      site = ProblemSite.valueOf(request.getSite().toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException e) {
      throw new InvalidProblemSiteException(request.getSite());
    }

    List<String> tagSlugs = Optional.ofNullable(request.getTagSlugs()).orElse(List.of());

    CreateProblemCommand command =
        new CreateProblemCommand(
            site,
            request.getSiteProblemId(),
            request.getTitle(),
            request.getUrl(),
            request.getDifficulty(),
            tagSlugs);

    return problemCommandPort.create(command);
  }
}
