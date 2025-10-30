package com.devouk.devouk_back;

import com.devouk.devouk_back.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.devouk.devouk_back")
@ConfigurationPropertiesScan(basePackageClasses = {AppProperties.class})
public class DevOukBackApplication {

  public static void main(String[] args) {
    SpringApplication.run(DevOukBackApplication.class, args);
  }
}
