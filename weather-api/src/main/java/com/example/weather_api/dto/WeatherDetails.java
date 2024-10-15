package com.example.weather_api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherDetails {
    String weatherDescription;
    double temperature;  // Temperature in Kelvin
    int humidity;        // Humidity percentage
    double windSpeed;    // Wind speed in m/s
}
