package com.karrostech.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class KafkaToolApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(KafkaToolApplication.class, args);
    }

}
