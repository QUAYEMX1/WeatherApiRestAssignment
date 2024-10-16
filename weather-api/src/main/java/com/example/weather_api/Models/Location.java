package com.example.weather_api.Models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int locationId;
    String pincode;
    double latitude;
    double longitude;

    @OneToMany(mappedBy = "location",cascade = CascadeType.ALL)
    List<WeatherInfo>weatherInfoList=new ArrayList<>();
}
