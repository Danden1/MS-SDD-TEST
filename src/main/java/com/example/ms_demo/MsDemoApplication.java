package com.example.ms_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsDemoApplication.class, args);
    }

}
