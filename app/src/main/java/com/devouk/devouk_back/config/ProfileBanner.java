package com.devouk.devouk_back.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProfileBanner {
  public ProfileBanner(@Value("${spring.profiles.active:default}") String activeProfiles) {
    log.info("=== ACTIVE PROFILE(S): {} ===", activeProfiles);
  }
}
