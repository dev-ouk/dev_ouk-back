package com.devouk.devouk_back.problem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.devouk.devouk_back.domain.common.exception.InvalidProblemSiteException;
import com.devouk.devouk_back.domain.problem.CreateProblemCommand;
import com.devouk.devouk_back.domain.problem.Problem;
import com.devouk.devouk_back.domain.problem.ProblemCommandPort;
import com.devouk.devouk_back.domain.problem.ProblemSite;
import com.devouk.devouk_back.problem.dto.ProblemCreateRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ProblemAppServiceTest {

  @Test
  void createProblem_validRequest_delegatesToPort_andReturnsProblem() {
    ProblemCommandPort port = mock(ProblemCommandPort.class);
    ProblemAppService service = new ProblemAppService(port);

    ProblemCreateRequest req = new ProblemCreateRequest();
    req.setSite("baekjoon"); // 소문자 → enum 변환 확인
    req.setSiteProblemId("1000");
    req.setTitle("A+B");
    req.setUrl("https://www.acmicpc.net/problem/1000");
    req.setDifficulty(1);
    req.setTagSlugs(List.of("implementation", "math"));

    given(port.create(any(CreateProblemCommand.class)))
        .willAnswer(
            invocation -> {
              CreateProblemCommand c = invocation.getArgument(0);
              return new Problem(
                  123L,
                  c.getSite(),
                  c.getSiteProblemId(),
                  c.getTitle(),
                  c.getUrl(),
                  c.getDifficulty(),
                  c.getTagSlugs());
            });

    // when
    Problem result = service.createProblem(req);

    // then
    verify(port).create(any(CreateProblemCommand.class));

    assertThat(result.getId()).isEqualTo(123L);
    assertThat(result.getSite()).isEqualTo(ProblemSite.BAEKJOON); // 대소문자 무시
    assertThat(result.getSiteProblemId()).isEqualTo("1000");
    assertThat(result.getTitle()).isEqualTo("A+B");
    assertThat(result.getUrl()).isEqualTo("https://www.acmicpc.net/problem/1000");
    assertThat(result.getDifficulty()).isEqualTo(1);
    assertThat(result.getTagSlugs()).containsExactlyInAnyOrder("implementation", "math");
  }

  @Test
  void createProblem_nullTagSlugs_isConvertedToEmptyListInCommand() {
    ProblemCommandPort port = mock(ProblemCommandPort.class);
    ProblemAppService service = new ProblemAppService(port);

    ProblemCreateRequest req = new ProblemCreateRequest();
    req.setSite("BAEKJOON");
    req.setSiteProblemId("1000");
    req.setTitle("A+B");
    req.setUrl("https://www.acmicpc.net/problem/1000");
    req.setDifficulty(1);
    req.setTagSlugs(null); // null → 빈 리스트로 바뀌는지 확인

    given(port.create(any(CreateProblemCommand.class)))
        .willAnswer(
            invocation -> {
              CreateProblemCommand c = invocation.getArgument(0);
              return new Problem(
                  1L,
                  c.getSite(),
                  c.getSiteProblemId(),
                  c.getTitle(),
                  c.getUrl(),
                  c.getDifficulty(),
                  c.getTagSlugs());
            });

    // when
    Problem result = service.createProblem(req);

    // then
    ArgumentCaptor<CreateProblemCommand> captor =
        ArgumentCaptor.forClass(CreateProblemCommand.class);
    verify(port).create(captor.capture());

    CreateProblemCommand sent = captor.getValue();
    assertThat(sent.getTagSlugs()).isNotNull().isEmpty();
    assertThat(result.getTagSlugs()).isEmpty();
  }

  @Test
  void createProblem_invalidSite_throwsInvalidProblemSiteException() {
    ProblemCommandPort port = mock(ProblemCommandPort.class);
    ProblemAppService service = new ProblemAppService(port);

    ProblemCreateRequest req = new ProblemCreateRequest();
    req.setSite("HACKERRANK");
    req.setSiteProblemId("1000");
    req.setTitle("A+B");
    req.setUrl("https://example.com/problem/1000");
    req.setDifficulty(1);

    assertThatThrownBy(() -> service.createProblem(req))
        .isInstanceOf(InvalidProblemSiteException.class)
        .hasMessageContaining("HACKERRANK");

    verify(port, never()).create(any());
  }
}
