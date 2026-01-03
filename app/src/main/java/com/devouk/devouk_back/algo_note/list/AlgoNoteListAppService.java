package com.devouk.devouk_back.algo_note.list;

import com.devouk.devouk_back.algo_note.list.AlgoNoteListCursorCodec.DecodedCursor;
import com.devouk.devouk_back.algo_note.list.dto.AlgoNotesResponse;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteListPage;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteListQuery;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteListQueryPort;
import com.devouk.devouk_back.domain.algo_note.AlgoNoteListSortOption;
import com.devouk.devouk_back.domain.common.exception.InvalidAlgoNoteSortOptionException;
import com.devouk.devouk_back.domain.common.exception.InvalidCreatedAtRangeException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AlgoNoteListAppService {

  private final AlgoNoteListQueryPort queryPort;
  private final AlgoNoteListCursorCodec cursorCodec;

  public AlgoNoteListAppService(
      AlgoNoteListQueryPort queryPort, AlgoNoteListCursorCodec cursorCodec) {
    this.queryPort = queryPort;
    this.cursorCodec = cursorCodec;
  }

  public AlgoNotesResponse search(
      String q,
      List<String> tagSlugs,
      OffsetDateTime createdAtFrom,
      OffsetDateTime createdAtTo,
      String sortParam,
      Integer size,
      String cursor) {

    int pageSize = (size == null || size <= 0) ? 20 : Math.min(size, 100);

    if (createdAtFrom != null && createdAtTo != null && createdAtFrom.isAfter(createdAtTo)) {
      throw new InvalidCreatedAtRangeException();
    }

    AlgoNoteListSortOption sort = convertSort(sortParam);
    DecodedCursor decoded = cursorCodec.decode(cursor);

    OffsetDateTime cursorCreatedAt = decoded != null ? decoded.getCreatedAt() : null;
    Long cursorNoteId = decoded != null ? decoded.getNoteId() : null;

    List<String> safeTagSlugs = Optional.ofNullable(tagSlugs).orElse(List.of());

    AlgoNoteListQuery query =
        new AlgoNoteListQuery(
            normalizeQ(q),
            safeTagSlugs,
            createdAtFrom,
            createdAtTo,
            sort,
            pageSize,
            cursorCreatedAt,
            cursorNoteId);

    AlgoNoteListPage page = queryPort.search(query);

    String nextCursor =
        page.isHasNext()
            ? cursorCodec.encode(page.getNextCursorCreatedAt(), page.getNextCursorNoteId())
            : null;

    return AlgoNotesResponse.from(page, nextCursor, sortToParam(sort));
  }

  private String normalizeQ(String q) {
    if (q == null) {
      return null;
    }
    String t = q.trim();
    return t.isEmpty() ? null : t;
  }

  private AlgoNoteListSortOption convertSort(String param) {
    if (param == null || param.isBlank()) {
      return AlgoNoteListSortOption.CREATED_AT_DESC;
    }
    return switch (param.toLowerCase()) {
      case "created_at_desc" -> AlgoNoteListSortOption.CREATED_AT_DESC;
      case "created_at_asc" -> AlgoNoteListSortOption.CREATED_AT_ASC;
      default -> throw new InvalidAlgoNoteSortOptionException(param);
    };
  }

  private String sortToParam(AlgoNoteListSortOption sort) {
    return switch (sort) {
      case CREATED_AT_DESC -> "created_at_desc";
      case CREATED_AT_ASC -> "created_at_asc";
    };
  }
}
