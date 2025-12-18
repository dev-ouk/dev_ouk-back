package com.devouk.devouk_back.problem.list;

import com.devouk.devouk_back.domain.common.exception.InvalidCursorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class ProblemListCursorCodec {

  private final ObjectMapper objectMapper;

  public ProblemListCursorCodec(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Getter
  @AllArgsConstructor
  public static class DecodedCursor {
    private final OffsetDateTime attemptedAt;
    private final Integer difficulty;
    private final Long problemId;
  }

  public String encodeForAttemptSort(OffsetDateTime attemptedAt, Long problemId) {
    if (attemptedAt == null || problemId == null) {
      return null;
    }
    try {
      String json =
          objectMapper.writeValueAsString(
              Map.of("attemptedAt", attemptedAt.toString(), "problemId", problemId));
      return Base64.getUrlEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Cursor encoding 실패", e);
    }
  }

  public String encodeForDifficultySort(Integer difficulty, Long problemId) {
    if (difficulty == null || problemId == null) {
      return null;
    }
    try {
      String json =
          objectMapper.writeValueAsString(
              Map.of(
                  "difficulty", difficulty,
                  "problemId", problemId));
      return Base64.getUrlEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Cursor encoding 실패", e);
    }
  }

  public DecodedCursor decode(String cursor) {
    if (cursor == null || cursor.isBlank()) {
      return null;
    }
    try {
      byte[] decoded = Base64.getUrlDecoder().decode(cursor);
      @SuppressWarnings("unchecked")
      Map<String, Object> map = objectMapper.readValue(decoded, Map.class);

      OffsetDateTime attemptedAt = null;
      Integer difficulty = null;
      Long problemId = null;

      Object at = map.get("attemptedAt");
      if (at != null) {
        try {
          attemptedAt = OffsetDateTime.parse(at.toString());
        } catch (DateTimeParseException e) {
          throw new IllegalStateException("Cursor Decoding 실패", e);
        }
      }

      Object diff = map.get("difficulty");
      if (diff instanceof Number n) {
        difficulty = n.intValue();
      } else if (diff != null) {
        difficulty = Integer.parseInt(diff.toString());
      }

      Object pid = map.get("problemId");
      if (pid instanceof Number n) {
        problemId = n.longValue();
      } else if (pid != null) {
        problemId = Long.parseLong(pid.toString());
      }

      if (problemId == null) {
        return null;
      }
      return new DecodedCursor(attemptedAt, difficulty, problemId);

    } catch (Exception e) {
      throw new InvalidCursorException();
    }
  }
}
