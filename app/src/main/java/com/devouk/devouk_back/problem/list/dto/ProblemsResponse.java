package com.devouk.devouk_back.problem.list.dto;

import com.devouk.devouk_back.domain.problem.ProblemCandidate;
import com.devouk.devouk_back.domain.problem.ProblemCandidatePage;
import com.devouk.devouk_back.domain.problem.ProblemLastAttemptSummary;
import com.devouk.devouk_back.domain.problem.ProblemSortOption;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemsResponse {

  private final List<Item> items;
  private final int size;
  private final boolean hasNext;
  private final String nextCursor;
  private final String sort;

  public static ProblemsResponse from(
      ProblemCandidatePage page, String encodedCursor, ProblemSortOption sortOption) {

    List<Item> mapped = page.getItems().stream().map(Item::from).toList();

    return new ProblemsResponse(
        mapped, page.getSize(), page.isHasNext(), encodedCursor, sortToParam(sortOption));
  }

  private static String sortToParam(ProblemSortOption sort) {
    return switch (sort) {
      case RECENT_ATTEMPT -> "recent_attempt";
      case OLDEST_ATTEMPT -> "oldest_attempt";
      case HIGHEST_DIFFICULTY -> "highest_difficulty";
      case LOWEST_DIFFICULTY -> "lowest_difficulty";
    };
  }

  @Getter
  @AllArgsConstructor
  public static class Item {
    private final String site;
    private final String siteProblemId;
    private final String title;
    private final Integer difficulty;
    private final LastAttempt lastAttempt;

    public static Item from(ProblemCandidate c) {
      LastAttempt last = c.getLastAttempt() != null ? LastAttempt.from(c.getLastAttempt()) : null;

      return new Item(
          c.getSite().name(), c.getSiteProblemId(), c.getTitle(), c.getDifficulty(), last);
    }
  }

  @Getter
  @AllArgsConstructor
  public static class LastAttempt {
    private final String verdict;
    private final String attemptedAt;

    public static LastAttempt from(ProblemLastAttemptSummary summary) {
      return new LastAttempt(
          summary.getVerdict().name(),
          summary.getAttemptedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }
  }
}
