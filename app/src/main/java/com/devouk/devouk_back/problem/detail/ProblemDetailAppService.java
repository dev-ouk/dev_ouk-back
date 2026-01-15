package com.devouk.devouk_back.problem.detail;

import com.devouk.devouk_back.domain.common.exception.InvalidProblemSiteException;
import com.devouk.devouk_back.domain.common.exception.ProblemNotFoundException;
import com.devouk.devouk_back.domain.problem.ProblemDetailQueryPort;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import com.devouk.devouk_back.problem.detail.dto.ProblemDetailResponse;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProblemDetailAppService {

  private final ProblemDetailQueryPort queryPort;

  public ProblemDetailAppService(ProblemDetailQueryPort queryPort) {
    this.queryPort = queryPort;
  }

  public ProblemDetailResponse get(String siteRaw, String siteProblemIdRaw) {
    ProblemSite site = parseSite(siteRaw);
    String siteProblemId = normalizeSiteProblemId(siteProblemIdRaw);

    return queryPort
        .findByKey(site, siteProblemId)
        .map(ProblemDetailResponse::from)
        .orElseThrow(() -> new ProblemNotFoundException(site, siteProblemId));
  }

  private ProblemSite parseSite(String raw) {
    try {
      return ProblemSite.valueOf(raw.trim().toUpperCase(Locale.ROOT));
    } catch (Exception e) {
      throw new InvalidProblemSiteException(raw);
    }
  }

  private String normalizeSiteProblemId(String raw) {
    if (raw == null) {
      return null;
    }
    String t = raw.trim();
    return t.isEmpty() ? null : t;
  }
}
