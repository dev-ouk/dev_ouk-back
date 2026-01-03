package com.devouk.devouk_back.algo_note.list;

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
public class AlgoNoteListCursorCodec {

  private final ObjectMapper objectMapper;

  public AlgoNoteListCursorCodec(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Getter
  @AllArgsConstructor
  public static class DecodedCursor {
    private final OffsetDateTime createdAt;
    private final Long noteId;
  }

  public String encode(OffsetDateTime createdAt, Long noteId) {
    if (createdAt == null || noteId == null) {
      return null;
    }
    try {
      String json =
          objectMapper.writeValueAsString(
              Map.of("createdAt", createdAt.toString(), "noteId", noteId));
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

      Object createdAtRaw = map.get("createdAt");
      Object noteIdRaw = map.get("noteId");

      if (createdAtRaw == null || noteIdRaw == null) {
        throw new InvalidCursorException();
      }

      OffsetDateTime createdAt;
      try {
        createdAt = OffsetDateTime.parse(createdAtRaw.toString());
      } catch (DateTimeParseException e) {
        throw new InvalidCursorException();
      }

      Long noteId;
      if (noteIdRaw instanceof Number n) {
        noteId = n.longValue();
      } else {
        noteId = Long.parseLong(noteIdRaw.toString());
      }

      return new DecodedCursor(createdAt, noteId);
    } catch (Exception e) {
      throw new InvalidCursorException();
    }
  }
}
