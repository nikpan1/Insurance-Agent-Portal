package com.policytracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PolicyTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PolicyTrackerApplication.class, args);
    }
}
