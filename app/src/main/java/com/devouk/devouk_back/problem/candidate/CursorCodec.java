package com.devouk.devouk_back.problem.candidate;

import com.devouk.devouk_back.domain.common.exception.InvalidCursorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CursorCodec {

  private final ObjectMapper objectMapper;

  public CursorCodec(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public String encode(Long lastProblemId) {
    if (lastProblemId == null) {
      return null;
    }
    try {
      String json = objectMapper.writeValueAsString(Map.of("lastProblemId", lastProblemId));
      return Base64.getUrlEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Cursor encoding 실패", e);
    }
  }

  public Long decode(String cursor) {
    if (cursor == null || cursor.isBlank()) {
      return null;
    }
    try {
      byte[] decoded = Base64.getUrlDecoder().decode(cursor);
      @SuppressWarnings("unchecked")
      Map<String, Object> map = objectMapper.readValue(decoded, Map.class);
      Object value = map.get("lastProblemId");
      if (value == null) {
        return null;
      }
      if (value instanceof Number n) {
        return n.longValue();
      }
      return Long.parseLong(value.toString());
    } catch (Exception e) {
      throw new InvalidCursorException();
    }
  }
}
