package com.devouk.devouk_back.config;

import com.devouk.devouk_back.domain.problem.ProblemPreviewPort;
import com.devouk.devouk_back.infra.problem.BaekjoonProblemSiteClient;
import com.devouk.devouk_back.infra.problem.CompositeProblemPreviewAdapter;
import com.devouk.devouk_back.infra.problem.ProblemSiteClient;
import com.devouk.devouk_back.infra.problem.ProgrammersProblemSiteClient;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ProblemPreviewConfig {

  @Bean
  public ProblemSiteClient baekjoonProblemSiteClient(RestClient.Builder builder) {
    return new BaekjoonProblemSiteClient(builder);
  }

  @Bean
  public ProblemSiteClient programmersProblemSiteClient(RestClient.Builder builder) {
    return new ProgrammersProblemSiteClient(builder);
  }

  @Bean
  public ProblemPreviewPort problemPreviewPort(List<ProblemSiteClient> siteClients) {
    return new CompositeProblemPreviewAdapter(siteClients);
  }
}
