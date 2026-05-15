package com.sb3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@SpringBootApplication
public class Sb3Application {

    public static void main(String[] args) {
        SpringApplication.run(Sb3Application.class, args);
        log.info("Application started");
    }

}
