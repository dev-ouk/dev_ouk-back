package com.devouk.devouk_back;

import org.springframework.boot.SpringApplication;

public class TestDevOukBackApplication {

    public static void main(String[] args) {
        SpringApplication.from(DevOukBackApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
