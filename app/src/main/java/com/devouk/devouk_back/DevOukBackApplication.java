package com.devouk.devouk_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.devouk.devouk_back")
public class DevOukBackApplication {

  public static void main(String[] args) {
    SpringApplication.run(DevOukBackApplication.class, args);
  }
}
