package com.devouk.devouk_back.infra.problem;

import com.devouk.devouk_back.domain.common.exception.ProblemPreviewFetchException;
import com.devouk.devouk_back.domain.problem.ProblemPreview;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

public class BaekjoonProblemSiteClient implements ProblemSiteClient {

  private final RestClient restClient;

  public BaekjoonProblemSiteClient(RestClient.Builder builder) {
    this.restClient = builder.baseUrl("https://solved.ac/api/v3").build();
  }

  @Override
  public boolean supports(URI uri) {
    String host = uri.getHost();
    return host != null && host.contains("acmicpc.net");
  }

  @Override
  public ProblemPreview fetchPreview(URI uri) {
    String problemId = extractProblemId(uri);
    SolvedAcProblemResponse response = callSolvedAc(problemId);
    List<String> algoSlugs = extractAlgoSlugs(response);

    return new ProblemPreview(
        ProblemSite.BAEKJOON,
        problemId,
        response.getTitleKo(),
        uri.toString(),
        response.getLevel(),
        Map.of("algo", algoSlugs));
  }

  private SolvedAcProblemResponse callSolvedAc(String problemId) {
    try {
      SolvedAcProblemResponse response =
          restClient
              .get()
              .uri("/problem/show?problemId={problemId}", problemId)
              .retrieve()
              .onStatus(
                  status -> status.is4xxClientError() || status.is5xxServerError(),
                  (req, res) -> {
                    throw new ProblemPreviewFetchException(
                        "solved.ac 호출 실패 (" + res.getStatusCode() + ")");
                  })
              .body(SolvedAcProblemResponse.class);

      if (response == null) {
        throw new ProblemPreviewFetchException("solved.ac 응답 파싱에 실패했습니다.");
      }

      return response;

    } catch (RestClientException e) {
      throw new ProblemPreviewFetchException("solved.ac 호출 중 오류가 발생했습니다.");
    }
  }

  private List<String> extractAlgoSlugs(SolvedAcProblemResponse response) {
    if (response.getTags() == null) {
      return List.of();
    }

    return response.getTags().stream().map(this::resolveTagSlugOrName).toList();
  }

  private String resolveTagSlugOrName(SolvedAcTag tag) {
    List<SolvedAcTag.DisplayName> displayNames = tag.getDisplayNames();

    if (displayNames != null) {
      return displayNames.stream()
          .filter(d -> "en".equals(d.getLanguage()))
          .map(SolvedAcTag.DisplayName::getName)
          .findFirst()
          .orElse(tag.getKey());
    }

    return tag.getKey();
  }

  private String extractProblemId(URI uri) {
    String path = uri.getPath();
    if (path == null) {
      throw new ProblemPreviewFetchException("백준 URL 형식이 올바르지 않습니다.");
    }

    String[] parts = path.split("/");
    if (parts.length < 3 || !"problem".equals(parts[1])) {
      throw new ProblemPreviewFetchException("백준 URL 형식이 올바르지 않습니다.");
    }

    return parts[2];
  }

  public static class SolvedAcProblemResponse {

    private int problemId;
    private String titleKo;
    private int level;
    private List<SolvedAcTag> tags;

    public int getProblemId() {
      return problemId;
    }

    public String getTitleKo() {
      return titleKo;
    }

    public int getLevel() {
      return level;
    }

    public List<SolvedAcTag> getTags() {
      return tags;
    }
  }

  public static class SolvedAcTag {
    private String key;
    private List<DisplayName> displayNames;

    public String getKey() {
      return key;
    }

    public List<DisplayName> getDisplayNames() {
      return displayNames;
    }

    public static class DisplayName {
      private String language;
      private String name;

      public String getLanguage() {
        return language;
      }

      public String getName() {
        return name;
      }
    }
  }
}
