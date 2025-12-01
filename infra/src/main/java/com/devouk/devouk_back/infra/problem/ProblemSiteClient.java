package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.problem.ProblemPreview;
import java.net.URI;

public interface ProblemSiteClient {

  boolean supports(URI uri);

  ProblemPreview fetchPreview(URI uri);
}
