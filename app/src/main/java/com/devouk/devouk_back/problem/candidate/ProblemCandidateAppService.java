package com.devouk.devouk_back.problem.candidate;

import com.devouk.devouk_back.domain.common.exception.InvalidProblemSiteException;
import com.devouk.devouk_back.domain.problem.ProblemCandidatePage;
import com.devouk.devouk_back.domain.problem.ProblemCandidateQuery;
import com.devouk.devouk_back.domain.problem.ProblemCandidateQueryPort;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import com.devouk.devouk_back.problem.candidate.dto.ProblemCandidatesResponse;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProblemCandidateAppService {

  private final ProblemCandidateQueryPort queryPort;
  private final CursorCodec cursorCodec;

  public ProblemCandidateAppService(ProblemCandidateQueryPort queryPort, CursorCodec cursorCodec) {
    this.queryPort = queryPort;
    this.cursorCodec = cursorCodec;
  }

  public ProblemCandidatesResponse search(
      String q, List<String> siteStrings, Integer size, String cursor) {
    int pageSize = (size == null || size <= 0) ? 20 : Math.min(size, 100);
    List<ProblemSite> sites = convertSites(siteStrings);
    Long cursorProblemId = cursorCodec.decode(cursor);

    ProblemCandidateQuery query = new ProblemCandidateQuery(q, sites, pageSize, cursorProblemId);

    ProblemCandidatePage page = queryPort.search(query);
    String nextCursor = cursorCodec.encode(page.getNextCursorProblemId());

    return ProblemCandidatesResponse.from(page, nextCursor);
  }

  private List<ProblemSite> convertSites(List<String> siteStrings) {
    if (siteStrings == null || siteStrings.isEmpty()) {
      return Collections.emptyList();
    }
    return siteStrings.stream()
        .map(
            s -> {
              try {
                return ProblemSite.valueOf(s.toUpperCase(Locale.ROOT));
              } catch (IllegalArgumentException e) {
                throw new InvalidProblemSiteException(s);
              }
            })
        .toList();
  }
}
