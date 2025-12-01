package com.devouk.devouk_back.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.devouk.devouk_back.domain.problem.ProblemPreviewPort;
import com.devouk.devouk_back.infra.problem.BaekjoonProblemSiteClient;
import com.devouk.devouk_back.infra.problem.CompositeProblemPreviewAdapter;
import com.devouk.devouk_back.infra.problem.ProblemSiteClient;
import com.devouk.devouk_back.infra.problem.ProgrammersProblemSiteClient;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class ProblemPreviewConfigTest {
  @Test
  void beans_areWired_asExpected() {
    ProblemPreviewConfig config = new ProblemPreviewConfig();

    RestClient.Builder builder = RestClient.builder();

    ProblemSiteClient baekjoon = config.baekjoonProblemSiteClient(builder);
    ProblemSiteClient programmers = config.programmersProblemSiteClient(builder);

    assertThat(baekjoon).isInstanceOf(BaekjoonProblemSiteClient.class);
    assertThat(programmers).isInstanceOf(ProgrammersProblemSiteClient.class);

    ProblemPreviewPort port = config.problemPreviewPort(List.of(baekjoon, programmers));

    assertThat(port).isInstanceOf(CompositeProblemPreviewAdapter.class);
  }
}
