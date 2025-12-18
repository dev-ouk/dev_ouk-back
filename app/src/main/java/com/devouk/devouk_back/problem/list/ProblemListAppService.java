package com.devouk.devouk_back.problem.list;

import com.devouk.devouk_back.domain.common.exception.InvalidAttemptVerdictException;
import com.devouk.devouk_back.domain.common.exception.InvalidProblemSortOptionException;
import com.devouk.devouk_back.domain.problem.*;
import com.devouk.devouk_back.problem.list.ProblemListCursorCodec.DecodedCursor;
import com.devouk.devouk_back.problem.list.dto.ProblemsResponse;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProblemListAppService {

  private final ProblemAttemptQueryPort queryPort;
  private final ProblemListCursorCodec cursorCodec;

  public ProblemListAppService(
      ProblemAttemptQueryPort queryPort, ProblemListCursorCodec cursorCodec) {
    this.queryPort = queryPort;
    this.cursorCodec = cursorCodec;
  }

  public ProblemsResponse search(
      String q,
      List<String> siteStrings,
      Integer difficultyMin,
      Integer difficultyMax,
      List<String> tagSlugs,
      String finalVerdictParam,
      OffsetDateTime attemptedFrom,
      OffsetDateTime attemptedTo,
      String sortParam,
      Integer size,
      String cursor) {

    int pageSize = (size == null || size <= 0) ? 20 : Math.min(size, 100);

    List<ProblemSite> sites = convertSites(siteStrings);
    AttemptVerdict finalVerdict = convertFinalVerdict(finalVerdictParam);
    ProblemSortOption sort = convertSort(sortParam);

    DecodedCursor decodedCursor = cursorCodec.decode(cursor);

    OffsetDateTime cursorAttemptedAt =
        decodedCursor != null ? decodedCursor.getAttemptedAt() : null;
    Integer cursorDifficulty = decodedCursor != null ? decodedCursor.getDifficulty() : null;
    Long cursorProblemId = decodedCursor != null ? decodedCursor.getProblemId() : null;

    List<String> safeTagSlugs = Optional.ofNullable(tagSlugs).orElse(List.of());

    ProblemAttemptQuery query =
        new ProblemAttemptQuery(
            normalizeQ(q),
            sites,
            difficultyMin,
            difficultyMax,
            safeTagSlugs,
            finalVerdict,
            attemptedFrom,
            attemptedTo,
            sort,
            pageSize,
            cursorAttemptedAt,
            cursorDifficulty,
            cursorProblemId);

    ProblemCandidatePage page = queryPort.search(query);

    String nextCursor = buildNextCursor(page, sort);

    return ProblemsResponse.from(page, nextCursor, sort);
  }

  private String normalizeQ(String q) {
    if (q == null) {
      return null;
    }
    String trimmed = q.trim();
    return trimmed.isEmpty() ? null : trimmed;
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
                // ProblemSite 변환 못하면 InvalidProblemSiteException 던질 수도 있지만
                // 여기서는 "sites 필터" 에서는 잘못된 값은 그냥 무시하는 쪽보다,
                // 확실히 오류를 내는게 낫다고 보면 BusinessException을 따로 만드는 것도 가능.
                throw new com.devouk.devouk_back.domain.common.exception
                    .InvalidProblemSiteException(s);
              }
            })
        .toList();
  }

  private AttemptVerdict convertFinalVerdict(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    try {
      return AttemptVerdict.valueOf(value.toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException e) {
      throw new InvalidAttemptVerdictException(value);
    }
  }

  private ProblemSortOption convertSort(String param) {
    if (param == null || param.isBlank()) {
      return ProblemSortOption.RECENT_ATTEMPT;
    }
    return switch (param.toLowerCase(Locale.ROOT)) {
      case "recent_attempt" -> ProblemSortOption.RECENT_ATTEMPT;
      case "oldest_attempt" -> ProblemSortOption.OLDEST_ATTEMPT;
      case "highest_difficulty" -> ProblemSortOption.HIGHEST_DIFFICULTY;
      case "lowest_difficulty" -> ProblemSortOption.LOWEST_DIFFICULTY;
      default -> throw new InvalidProblemSortOptionException(param);
    };
  }

  private String buildNextCursor(ProblemCandidatePage page, ProblemSortOption sort) {
    if (!page.isHasNext() || page.getItems().isEmpty()) {
      return null;
    }

    var lastItem = page.getItems().get(page.getItems().size() - 1);
    Long problemId = page.getNextCursorProblemId();

    switch (sort) {
      case RECENT_ATTEMPT, OLDEST_ATTEMPT -> {
        if (lastItem.getLastAttempt() == null) {
          return null;
        }
        return cursorCodec.encodeForAttemptSort(
            lastItem.getLastAttempt().getAttemptedAt(), problemId);
      }
      case HIGHEST_DIFFICULTY, LOWEST_DIFFICULTY -> {
        Integer diff = lastItem.getDifficulty();
        if (diff == null) {
          return null;
        }
        return cursorCodec.encodeForDifficultySort(diff, problemId);
      }
      default -> {
        return null;
      }
    }
  }
}
