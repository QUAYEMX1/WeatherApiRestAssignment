package com.example.weather_api.Models;

import com.example.weather_api.dto.WeatherDetails;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class weatherInfo {

    @Id
    String pincode;

    @Embedded // Use @Embedded to save complex objects
    WeatherDetails weatherDetails;

    @Column(name = "weather_date")
    LocalDate date;

}
