package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.common.exception.ProblemPreviewFetchException;
import com.devouk.devouk_back.domain.problem.ProblemPreview;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

public class ProgrammersProblemSiteClient implements ProblemSiteClient {

  private final RestClient restClient;

  public ProgrammersProblemSiteClient(RestClient.Builder builder) {
    this.restClient = builder.build();
  }

  @Override
  public boolean supports(URI uri) {
    String host = uri.getHost();
    return host != null && host.contains("programmers.co.kr");
  }

  @Override
  public ProblemPreview fetchPreview(URI uri) {
    String lessonId = extractLessonId(uri);

    String html = fetchHtml(uri);
    Document doc = Jsoup.parse(html);

    String title = extractTitle(doc);
    Integer level = extractLevel(doc);

    Map<String, List<String>> taxonomies = Collections.emptyMap();

    return new ProblemPreview(
        ProblemSite.PROGRAMMERS, lessonId, title, uri.toString(), level, taxonomies);
  }

  private String fetchHtml(URI uri) {
    try {
      String html =
          restClient
              .get()
              .uri(uri.toString())
              .header(
                  "User-Agent",
                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                      + "AppleWebKit/537.36 (KHTML, like Gecko) "
                      + "Chrome/120.0 Safari/537.36")
              .retrieve()
              .body(String.class);
      if (html == null) {
        throw new ProblemPreviewFetchException("프로그래머스 응답이 비어 있습니다.");
      }
      return html;
    } catch (RestClientException e) {
      throw new ProblemPreviewFetchException("프로그래머스 페이지 크롤링 중 오류가 발생했습니다.");
    }
  }

  private String extractTitle(Document doc) {
    Element lessonDiv = doc.selectFirst("div.lesson-content");
    if (lessonDiv != null) {
      String title = lessonDiv.attr("data-lesson-title");
      if (title != null && !title.isBlank()) {
        return title;
      }
    }
    Element og = doc.selectFirst("meta[property=og:title]");
    if (og != null) {
      String title = og.attr("content");
      if (title != null && !title.isBlank()) {
        return title.replace("코딩테스트 연습 -", "").trim();
      }
    }
    return doc.title();
  }

  private Integer extractLevel(Document doc) {
    Element lessonDiv = doc.selectFirst("div.lesson-content");
    if (lessonDiv == null) {
      return null;
    }

    String levelStr = lessonDiv.attr("data-challenge-level");
    if (levelStr == null || levelStr.isBlank()) {
      return null;
    }

    try {
      return Integer.parseInt(levelStr);
    } catch (NumberFormatException e) {
      // 여기서 EmptyCatchBlock 피함 + 디버깅 정보 남김
      //            log.warn("프로그래머스 난이도 파싱 실패. levelStr={}", levelStr, e);
      return null;
    }
  }

  private String extractLessonId(URI uri) {
    String path = uri.getPath();
    if (path == null) {
      throw new ProblemPreviewFetchException("프로그래머스 URL 형식이 올바르지 않습니다.");
    }

    String[] parts = path.split("/");
    for (int i = 0; i < parts.length - 1; i++) {
      if ("lessons".equals(parts[i])) {
        return parts[i + 1];
      }
    }
    throw new ProblemPreviewFetchException("lessons/{id} 형태의 URL이 아닙니다.");
  }
}
