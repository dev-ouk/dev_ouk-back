package com.devouk.devouk_back.infra.cors;

import java.util.List;

public class CorsSourceBuilder {

  private final List<String> allowedOrigins;

  public CorsSourceBuilder(List<String> allowedOrigins) {
    this.allowedOrigins = allowedOrigins;
  }

  public List<String> getAllowedOrigins() {
    return allowedOrigins;
  }
}
