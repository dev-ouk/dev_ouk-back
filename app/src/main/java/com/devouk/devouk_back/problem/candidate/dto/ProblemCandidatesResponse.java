package com.devouk.devouk_back.problem.candidate.dto;

import com.devouk.devouk_back.domain.problem.ProblemCandidate;
import com.devouk.devouk_back.domain.problem.ProblemCandidatePage;
import com.devouk.devouk_back.domain.problem.ProblemLastAttemptSummary;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemCandidatesResponse {

  private final List<Item> items;
  private final int size;
  private final boolean hasNext;
  private final String nextCursor;

  public static ProblemCandidatesResponse from(ProblemCandidatePage page, String encodedCursor) {
    List<Item> mapped = page.getItems().stream().map(Item::from).toList();

    return new ProblemCandidatesResponse(mapped, page.getSize(), page.isHasNext(), encodedCursor);
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
