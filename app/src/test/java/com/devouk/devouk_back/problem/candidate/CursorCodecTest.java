package com.devouk.devouk_back.problem.candidate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.devouk.devouk_back.domain.common.exception.InvalidCursorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CursorCodecTest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final CursorCodec codec = new CursorCodec(objectMapper);

  @Test
  void encode_returnsNull_whenIdIsNull() {
    assertThat(codec.encode(null)).isNull();
  }

  @Test
  void encodeAndDecode_roundTrip_withNumberValue() {
    String encoded = codec.encode(123L);

    Long decoded = codec.decode(encoded);

    assertThat(decoded).isEqualTo(123L);
  }

  @Test
  void decode_nullOrBlank_returnsNull() {
    assertThat(codec.decode(null)).isNull();
    assertThat(codec.decode("")).isNull();
    assertThat(codec.decode("   ")).isNull();
  }

  @Test
  void decode_withStringValue_parsesLong() throws Exception {
    String json = objectMapper.writeValueAsString(Map.of("lastProblemId", "42"));
    String base64 = Base64.getUrlEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));

    Long decoded = codec.decode(base64);

    assertThat(decoded).isEqualTo(42L);
  }

  @Test
  void decode_whenKeyMissing_returnsNull() throws Exception {
    String json = objectMapper.writeValueAsString(Map.of("other", 1));
    String base64 = Base64.getUrlEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));

    Long decoded = codec.decode(base64);

    assertThat(decoded).isNull();
  }

  @Test
  void decode_invalidBase64_throwsInvalidCursorException() {
    assertThatThrownBy(() -> codec.decode("###not-base64###"))
        .isInstanceOf(InvalidCursorException.class);
  }

  // encode 쪽의 catch 분기(IllegalStateException)까지 태우기 위한 failing mapper
  static class FailingMapper extends ObjectMapper {
    @Override
    public String writeValueAsString(Object value) throws JsonProcessingException {
      throw new JsonProcessingException("boom") {};
    }
  }

  @Test
  void encode_whenJsonProcessingFails_throwsIllegalStateException() {
    CursorCodec failingCodec = new CursorCodec(new FailingMapper());

    assertThatThrownBy(() -> failingCodec.encode(1L))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Cursor encoding 실패");
  }
}
