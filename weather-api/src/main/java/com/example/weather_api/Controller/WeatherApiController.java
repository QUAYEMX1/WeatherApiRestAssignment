package com.example.weather_api.Controller;

import com.example.weather_api.Models.weatherInfo;
import com.example.weather_api.Service.WeatherApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/weather")
public class WeatherApiController {

    @Autowired
    WeatherApiService weatherApiService;

    @GetMapping("/weatherInfo")
    public ResponseEntity<weatherInfo> retrieveWeatherInfo(@RequestParam("pincode")String pincode,
                                                           @RequestParam("for_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate forDate) throws Exception{
        weatherInfo response=weatherApiService.getWeatherInfo(pincode,forDate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
