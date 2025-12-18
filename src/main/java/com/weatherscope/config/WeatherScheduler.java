package com.weatherscope.config;

import com.weatherscope.service.WeatherService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherScheduler {
    private final WeatherService weatherService;
    @Value("${weather.cities}")
    private String citiesConfig;
    private List<String> cities;

    @PostConstruct
    public void init() {
        cities = Arrays.asList(citiesConfig.split(","));
    }

    private final MeterRegistry meterRegistry;

    //15 min
    @Scheduled(fixedRate = 15 * 60 * 1000)
//    @Scheduled(cron = "0 0 * * * *")
    public void updateWeather() {
        log.info("🌦 Starting scheduled weather update...");

        for (String city : cities) {
            try {
                weatherService.fetchAndSave(city);
                log.info("✅ Updated weather for {}", city);
                meterRegistry.counter("weather.scheduled.updates", "city", city).increment();
            } catch (Exception e) {
                log.error("❌ Failed to update weather for {}: {}", city, e.getMessage());
            }
        }

        log.info("🌤 Finished scheduled weather update.");
    }
}
