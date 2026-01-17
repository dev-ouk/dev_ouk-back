package com.devouk.devouk_back.error;

import com.devouk.devouk_back.domain.common.exception.ErrorCode;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorCodeHttpStatusMapper {

  private static final Map<ErrorCode, HttpStatus> MAP = new EnumMap<>(ErrorCode.class);

  static {
    // 404
    MAP.put(ErrorCode.MEMBER_NOT_FOUND, HttpStatus.NOT_FOUND);
    MAP.put(ErrorCode.TAXONOMY_NOT_FOUND, HttpStatus.NOT_FOUND);
    MAP.put(ErrorCode.PROBLEM_NOT_FOUND, HttpStatus.NOT_FOUND);
    MAP.put(ErrorCode.PROBLEM_TAG_NOT_FOUND, HttpStatus.NOT_FOUND);
    MAP.put(ErrorCode.ALGO_NOTE_NOT_FOUND, HttpStatus.NOT_FOUND);
    MAP.put(ErrorCode.ALGO_NOTE_TAG_NOT_FOUND, HttpStatus.NOT_FOUND);
    MAP.put(ErrorCode.ATTEMPT_NOT_FOUND, HttpStatus.NOT_FOUND);
    MAP.put(ErrorCode.ALGO_NOTE_SLUGS_NOT_FOUND, HttpStatus.NOT_FOUND);

    // 409
    MAP.put(ErrorCode.DUPLICATE_MEMBER, HttpStatus.CONFLICT);
    MAP.put(ErrorCode.DUPLICATE_PROBLEM, HttpStatus.CONFLICT);
    MAP.put(ErrorCode.DUPLICATE_ALGO_NOTE_SLUG, HttpStatus.CONFLICT);

    // 400
    MAP.put(ErrorCode.INVALID_PROBLEM_SITE, HttpStatus.BAD_REQUEST);
    MAP.put(ErrorCode.INVALID_PROBLEM_SORT_OPTION, HttpStatus.BAD_REQUEST);
    MAP.put(ErrorCode.UNSUPPORTED_PROBLEM_SITE, HttpStatus.BAD_REQUEST);

    MAP.put(ErrorCode.INVALID_ATTEMPT_VERDICT, HttpStatus.BAD_REQUEST);
    MAP.put(ErrorCode.INVALID_ATTEMPT_VERDICT_FOR_CREATE, HttpStatus.BAD_REQUEST);
    MAP.put(ErrorCode.INVALID_ATTEMPT_LANGUAGE, HttpStatus.BAD_REQUEST);
    MAP.put(ErrorCode.INVALID_ATTEMPT_FAIL_TYPE, HttpStatus.BAD_REQUEST);

    MAP.put(ErrorCode.INVALID_ALGO_NOTE_STATUS, HttpStatus.BAD_REQUEST);
    MAP.put(ErrorCode.INVALID_ALGO_NOTE_SORT_OPTION, HttpStatus.BAD_REQUEST);
    MAP.put(ErrorCode.INVALID_ALGO_NOTE_TITLE, HttpStatus.BAD_REQUEST);

    MAP.put(ErrorCode.INVALID_CURSOR, HttpStatus.BAD_REQUEST);
    MAP.put(ErrorCode.INVALID_CREATED_AT_RANGE, HttpStatus.BAD_REQUEST);

    MAP.put(ErrorCode.INVALID_ACTIVITY_TYPE, HttpStatus.BAD_REQUEST);
    MAP.put(ErrorCode.INVALID_ACTIVITY_GROUP_BY, HttpStatus.BAD_REQUEST);
    MAP.put(ErrorCode.INVALID_ACTIVITY_TIME_ZONE, HttpStatus.BAD_REQUEST);
    MAP.put(ErrorCode.INVALID_ACTIVITY_DATE_RANGE, HttpStatus.BAD_REQUEST);

    // 502
    MAP.put(ErrorCode.PROBLEM_PREVIEW_FETCH_FAILED, HttpStatus.BAD_GATEWAY);
  }

  public HttpStatus map(ErrorCode errorCode) {
    return MAP.getOrDefault(errorCode, HttpStatus.BAD_REQUEST);
  }
}
