package com.devouk.devouk_back.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
public class ProfileBannerTest {
    @Test
    void construct_withProfile_succeeds() {
        new ProfileBanner("dev");
    }
    @Test
    void construct_withoutProfile_usesDefault() {
        new ProfileBanner("default");
    }

    @Test
    void logActiveProfile_printsProfile(CapturedOutput output) {
        new ProfileBanner("dev");
        assertThat(output.getOut()).contains("=== ACTIVE PROFILE(S): dev ===");
    }
}
