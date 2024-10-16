package com.example.weather_api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof WeatherDetails)) return false;
        WeatherDetails that=(WeatherDetails) o;
        return Double.compare(that.temperature,temperature)==0 &&
                humidity== that.humidity &&
                Double.compare(that.windSpeed, windSpeed) ==0 &&
                Objects.equals(weatherDescription,that.weatherDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weatherDescription, temperature, humidity, windSpeed);
    }
}
