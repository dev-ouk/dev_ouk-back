package com.devouk.devouk_back.attempt;

import com.devouk.devouk_back.attempt.dto.AttemptCreateRequest;
import com.devouk.devouk_back.domain.common.exception.InvalidAttemptFailTypeException;
import com.devouk.devouk_back.domain.common.exception.InvalidAttemptLanguageException;
import com.devouk.devouk_back.domain.common.exception.InvalidAttemptVerdictForCreateException;
import com.devouk.devouk_back.domain.common.exception.InvalidProblemSiteException;
import com.devouk.devouk_back.domain.problem.*;
import java.time.OffsetDateTime;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttemptAppService {

  private final AttemptCommandPort commandPort;

  public AttemptAppService(AttemptCommandPort commandPort) {
    this.commandPort = commandPort;
  }

  public AttemptCreateResult create(AttemptCreateRequest req) {
    ProblemSite site = parseSite(req.getSite());
    AttemptLanguage language = parseLanguage(req.getLanguage());
    AttemptVerdict verdict = parseVerdict(req.getVerdict());
    AttemptFailType failType = parseFailType(req.getFailType());

    OffsetDateTime attemptedAt =
        req.getAttemptedAt() != null ? req.getAttemptedAt() : OffsetDateTime.now();

    CreateAttemptCommand cmd =
        new CreateAttemptCommand(
            site,
            req.getSiteProblemId().trim(),
            req.getTimeSpent(),
            language,
            req.getNotes(),
            verdict,
            req.getCode(),
            attemptedAt,
            failType,
            req.getFailDetail(),
            req.getSolution(),
            req.getNextReviewAt());

    return commandPort.create(cmd);
  }

  private ProblemSite parseSite(String raw) {
    try {
      return ProblemSite.valueOf(raw.trim().toUpperCase(Locale.ROOT));
    } catch (Exception e) {
      throw new InvalidProblemSiteException(raw);
    }
  }

  private AttemptLanguage parseLanguage(String raw) {
    try {
      AttemptLanguage parsed = AttemptLanguage.parse(raw);
      if (parsed == null) {
        throw new IllegalArgumentException();
      }
      return parsed;
    } catch (Exception e) {
      throw new InvalidAttemptLanguageException(raw);
    }
  }

  private AttemptFailType parseFailType(String raw) {
    try {
      return AttemptFailType.parseNullable(raw);
    } catch (Exception e) {
      throw new InvalidAttemptFailTypeException(raw);
    }
  }

  private AttemptVerdict parseVerdict(String raw) {
    if (raw == null || raw.isBlank()) {
      throw new InvalidAttemptVerdictForCreateException(raw);
    }

    try {
      return AttemptVerdict.valueOf(raw.trim().toUpperCase(Locale.ROOT));
    } catch (Exception e) {
      throw new InvalidAttemptVerdictForCreateException(raw);
    }
  }
}
