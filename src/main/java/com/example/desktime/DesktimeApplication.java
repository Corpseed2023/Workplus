package com.example.desktime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class DesktimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesktimeApplication.class, args);
    }

}
