package com.devouk.devouk_back.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {

  @NotBlank(message = "app.name 은 비울 수 없습니다.")
  private String name;

  @Valid private Cors cors = new Cors();

  @Getter
  @Setter
  public static class Cors {
    @Size(min = 1, message = "app.cors.allowed-origins 는 1개 이상이어야 합니다.")
    private List<String> allowedOrigins;
  }
}
