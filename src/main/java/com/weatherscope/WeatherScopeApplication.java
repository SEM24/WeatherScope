package com.weatherscope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeatherScopeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherScopeApplication.class, args);
    }

}
