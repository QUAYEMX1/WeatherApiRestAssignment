package com.example.weather_api.Repository;

import com.example.weather_api.Models.Location;
import com.example.weather_api.Models.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherApiRepository extends JpaRepository<WeatherInfo,Integer> {


}
