package com.weatherscope.repository;

import com.weatherscope.model.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherData, Long> {
    @Query("SELECT AVG(w.temperature) FROM WeatherData w WHERE w.city = :city AND w.timestamp >= :from")
    Double findAverageTempForCitySince(String city, LocalDateTime from);

    //Need to get the weather history for N days in city X
    List<WeatherData> findByCityAndTimestampAfterOrderByTimestampAsc(String city, LocalDateTime timestamp);

    List<WeatherData> findTop10ByCityOrderByTimestampDesc(String city);
}
