package com.weatherscope.controller;

import com.weatherscope.model.entity.WeatherData;
import com.weatherscope.model.entity.dto.AverageWeatherDto;
import com.weatherscope.service.WeatherService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/{city}")
    public WeatherData getWeather(@PathVariable("city") String city) {
        return weatherService.fetchAndSave(city);
    }

    @GetMapping("/avg/{city}")
    public ResponseEntity<AverageWeatherDto> getAverage(
            @PathVariable String city,
            @RequestParam @Min(1) @Max(365) int days
    ) {
        AverageWeatherDto avg = weatherService.getAverageTemp(city, days);
        return ResponseEntity.ok(avg);
    }

    @GetMapping("/trends/{city}")
    public ResponseEntity<List<WeatherData>> getTrendsCity(
            @PathVariable String city,
            @RequestParam @Min(1) @Max(365) int days
    ) {
        List<WeatherData> weatherData = weatherService.getWeatherHistoryForNDays(city, days);
        return ResponseEntity.ok(weatherData);
    }

    @GetMapping("/history/{city}")
    public List<WeatherData> getHistory(@PathVariable("city") String city) {
        return weatherService.getHistory(city);
    }
}
