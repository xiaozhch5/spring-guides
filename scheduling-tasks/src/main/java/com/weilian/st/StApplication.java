package com.weilian.st;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StApplication {

    public static void main(String[] args) {
        SpringApplication.run(StApplication.class, args);
    }

}
