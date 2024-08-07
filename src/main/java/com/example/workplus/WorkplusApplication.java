package com.example.workplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class WorkplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkplusApplication.class, args);
    }

}
