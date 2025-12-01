package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.common.exception.ProblemPreviewFetchException;
import com.devouk.devouk_back.domain.common.exception.UnsupportedProblemSiteException;
import com.devouk.devouk_back.domain.problem.ProblemPreview;
import com.devouk.devouk_back.domain.problem.ProblemPreviewPort;
import java.net.URI;
import java.util.List;

public class CompositeProblemPreviewAdapter implements ProblemPreviewPort {

  private final List<ProblemSiteClient> siteClients;

  public CompositeProblemPreviewAdapter(List<ProblemSiteClient> siteClients) {
    this.siteClients = List.copyOf(siteClients);
  }

  @Override
  public ProblemPreview getPreview(String url) {
    URI uri;
    try {
      uri = URI.create(url);
    } catch (IllegalArgumentException e) {
      throw new ProblemPreviewFetchException("잘못된 URL 형식입니다.");
    }

    return siteClients.stream()
        .filter(client -> client.supports(uri))
        .findFirst()
        .map(client -> client.fetchPreview(uri))
        .orElseThrow(() -> new UnsupportedProblemSiteException(url));
  }
}
