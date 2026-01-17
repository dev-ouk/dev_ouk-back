package com.devouk.devouk_back.infra.activity;

import com.devouk.devouk_back.domain.activity.ActivityHeatmapItem;
import com.devouk.devouk_back.domain.activity.ActivityHeatmapQuery;
import com.devouk.devouk_back.domain.activity.ActivityHeatmapQueryPort;
import com.devouk.devouk_back.domain.activity.ActivityHeatmapResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JpaActivityHeatmapQueryRepository implements ActivityHeatmapQueryPort {

  private static final String CODING_TEST_HEATMAP_SQL =
      """
            with first_ac as (
              select
                a.problem_id as problem_id,
                min(a.attempted_at) as first_ac_at
              from ct_attempt a
              where a.verdict = 'AC'
              group by a.problem_id
            )
            select
              (timezone(:tz, fa.first_ac_at))::date as d,
              count(*) as cnt
            from first_ac fa
            where (timezone(:tz, fa.first_ac_at))::date between :fromDate and :toDate
            group by d
            order by d asc
            """;

  private final EntityManager em;

  public JpaActivityHeatmapQueryRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public ActivityHeatmapResult getHeatmap(ActivityHeatmapQuery query) {
    return switch (query.getType()) {
      case CODING_TEST -> ActivityHeatmapResult.of(fetchCodingTestHeatmap(query));
    };
  }

  private List<ActivityHeatmapItem> fetchCodingTestHeatmap(ActivityHeatmapQuery q) {
    Query nq = createCodingTestHeatmapQuery(q);
    List<Object[]> rows = getRows(nq);
    return mapToItems(rows);
  }

  private Query createCodingTestHeatmapQuery(ActivityHeatmapQuery q) {
    Query nq = em.createNativeQuery(CODING_TEST_HEATMAP_SQL);
    nq.setParameter("tz", q.getTimeZone().getId());
    nq.setParameter("fromDate", Date.valueOf(q.getFrom()));
    nq.setParameter("toDate", Date.valueOf(q.getTo()));
    return nq;
  }

  @SuppressWarnings("unchecked")
  private List<Object[]> getRows(Query nq) {
    return (List<Object[]>) nq.getResultList();
  }

  private List<ActivityHeatmapItem> mapToItems(List<Object[]> rows) {
    List<ActivityHeatmapItem> result = new ArrayList<>();
    for (Object[] r : rows) {
      LocalDate date = parseLocalDate(r[0]);
      if (date == null) {
        continue;
      }
      long cnt = parseLong(r[1]);
      result.add(new ActivityHeatmapItem(date, cnt));
    }
    return result;
  }

  private LocalDate parseLocalDate(Object dObj) {
    if (dObj == null) {
      return null;
    }
    if (dObj instanceof Date dd) {
      return dd.toLocalDate();
    }
    // postgres 쿼리 결과가 LocalDate로 바로 올 수도 있음 (드라이버/설정에 따라)
    if (dObj instanceof LocalDate ld) {
      return ld;
    }
    return LocalDate.parse(dObj.toString());
  }

  private long parseLong(Object cObj) {
    if (cObj == null) {
      return 0L;
    }
    if (cObj instanceof Number n) {
      return n.longValue();
    }
    return Long.parseLong(cObj.toString());
  }
}
