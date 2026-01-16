package com.devouk.devouk_back.attempt;

import com.devouk.devouk_back.domain.common.exception.InvalidProblemSiteException;
import com.devouk.devouk_back.domain.problem.AttemptHistoryQueryPort;
import com.devouk.devouk_back.domain.problem.AttemptHistoryResult;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AttemptQueryAppService {

  private final AttemptHistoryQueryPort queryPort;

  public AttemptQueryAppService(AttemptHistoryQueryPort queryPort) {
    this.queryPort = queryPort;
  }

  public AttemptHistoryResult getAll(String siteRaw, String siteProblemIdRaw) {
    ProblemSite site = parseSite(siteRaw);
    String siteProblemId = normalizeSiteProblemId(siteProblemIdRaw);
    return queryPort.findAllByProblemKey(site, siteProblemId);
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
