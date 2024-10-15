package com.example.weather_api.Repository;

import com.example.weather_api.Models.weatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherApiRepository extends JpaRepository<weatherInfo,String> {

    Optional<weatherInfo> findByPincodeAndDate(String pincode, LocalDate forDate);

}
