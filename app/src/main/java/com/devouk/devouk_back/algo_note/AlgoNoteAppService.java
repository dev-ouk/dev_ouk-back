package com.devouk.devouk_back.algo_note;

import com.devouk.devouk_back.algo_note.dto.AlgoNoteCreateRequest;
import com.devouk.devouk_back.domain.algo_note.AlgoNote;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteCommandPort;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteStatus;
import com.devouk.devouk_back.domain.algo_note.CreateAlgoNoteCommand;
import com.devouk.devouk_back.domain.common.exception.InvalidAlgoNoteStatusException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlgoNoteAppService {
  private final AlgoNoteCommandPort commandPort;
  private final ObjectMapper objectMapper;

  public AlgoNoteAppService(AlgoNoteCommandPort commandPort, ObjectMapper objectMapper) {
    this.commandPort = commandPort;
    this.objectMapper = objectMapper;
  }

  public AlgoNote create(AlgoNoteCreateRequest request) {
    AlgoNoteStatus status = parseStatus(request.getStatus());

    boolean isPublic = Optional.ofNullable(request.getIsPublic()).orElse(false);
    boolean isPin = Optional.ofNullable(request.getIsPin()).orElse(false);
    List<String> tagSlugs = Optional.ofNullable(request.getTagSlugs()).orElse(List.of());

    String contentJsonString = toJsonString(request);

    CreateAlgoNoteCommand command =
        new CreateAlgoNoteCommand(
            request.getTitle(),
            request.getSlug(),
            contentJsonString,
            request.getContentHtml(),
            request.getContentText(),
            status,
            isPublic,
            isPin,
            tagSlugs);

    return commandPort.create(command);
  }

  private AlgoNoteStatus parseStatus(String value) {
    if (value == null || value.isBlank()) {
      throw new InvalidAlgoNoteStatusException(value);
    }
    try {
      return AlgoNoteStatus.valueOf(value.trim().toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException e) {
      throw new InvalidAlgoNoteStatusException(value);
    }
  }

  private String toJsonString(AlgoNoteCreateRequest request) {
    try {
      return objectMapper.writeValueAsString(request.getContentJson());
    } catch (JsonProcessingException e) {
      // contentJson 자체는 JsonNode라 보통 여기까지 안 오지만,
      // 확장 시(커스텀 노드 등) 대비해서 BusinessException으로 떨어뜨려도 됨.
      throw new IllegalStateException("contentJson 직렬화에 실패했습니다.", e);
    }
  }
}
