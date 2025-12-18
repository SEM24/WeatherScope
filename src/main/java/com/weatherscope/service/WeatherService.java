package com.weatherscope.service;

import com.weatherscope.exception.GlobalServiceException;
import com.weatherscope.model.entity.WeatherData;
import com.weatherscope.model.entity.dto.AverageWeatherDto;
import com.weatherscope.repository.WeatherRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {
    private final WebClient webClient;
    private final WeatherRepository weatherRepository;
    private final MeterRegistry meterRegistry;
    @Value("${openweather.api.key}")
    private String apiKey;

    public WeatherData fetchAndSave(String city) {
        long startTime = System.currentTimeMillis();
        String normalizedCity = city.trim().toLowerCase();
        try {
            log.info("Fetching weather data for city: {}", city);
            Map response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/data/2.5/weather")
                            .queryParam("q", city)
                            .queryParam("units", "metric")
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(); // block, not reactive
            if (response == null) {
                throw new GlobalServiceException(HttpStatus.BAD_REQUEST,"Empty response from weather API");
            }
            Map<String, Object> main = (Map<String, Object>) response.get("main");
            List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
            String description = (String) weatherList.getFirst().get("description");

            WeatherData weatherData = WeatherData.builder()
                    .city(normalizedCity)
                    .temperature(((Number) main.get("temp")).doubleValue())
                    .humidity(((Number) main.get("humidity")).doubleValue())
                    .description(description)
                    .timestamp(LocalDateTime.now())
                    .build();
            WeatherData saved = weatherRepository.save(weatherData);
            log.info("Weather data saved successfully for {}: {}°C, {}",
                    city, weatherData.getTemperature(), weatherData.getDescription());
            meterRegistry.counter("weather.api.calls", "city", normalizedCity, "status", "success").increment();

            meterRegistry.timer("weather.api.duration", "city", normalizedCity)
                    .record(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
            meterRegistry.gauge("weather.temperature", Tags.of("city", normalizedCity), weatherData.getTemperature());

            meterRegistry.gauge("weather.humidity", Tags.of("city", normalizedCity), weatherData.getHumidity());
            return saved;

        } catch (WebClientResponseException e) {
            log.error("API returned error {}: {}", e.getRawStatusCode(), e.getResponseBodyAsString());
            throw new GlobalServiceException(HttpStatus.BAD_REQUEST,"API error: " + e.getStatusCode(), e);
        } catch (Exception e) {
            meterRegistry.counter("weather.api.calls", "city", normalizedCity, "status", "error").increment();
            log.error("Failed to fetch weather for {}: {}", normalizedCity, e.getMessage());
            throw new GlobalServiceException(HttpStatus.BAD_REQUEST,"Failed to fetch weather data for " + normalizedCity, e);
        }
    }

    public AverageWeatherDto getAverageTemp(String city, int days) {
        LocalDateTime from = LocalDateTime.now().minusDays(days);
        Double averageTempForCitySince = weatherRepository.findAverageTempForCitySince(city.trim().toLowerCase(), from);
        return new AverageWeatherDto(city, days, averageTempForCitySince);
    }

    public List<WeatherData> getWeatherHistoryForNDays(String city, int days) {
        LocalDateTime from = LocalDateTime.now().minusDays(days);
        return weatherRepository.findByCityAndTimestampAfterOrderByTimestampAsc(city.trim().toLowerCase(), from);
    }

    public List<WeatherData> getHistory(String city) {
        log.info("Fetching history for city: {}: {}", city);
        //TODO
        return weatherRepository.findTop10ByCityOrderByTimestampDesc(city.trim().toLowerCase());
    }
}
