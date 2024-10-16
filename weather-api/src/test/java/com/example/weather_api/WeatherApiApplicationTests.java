package com.example.weather_api;

import com.example.weather_api.Models.Location;
import com.example.weather_api.Models.WeatherInfo;
import com.example.weather_api.Repository.LocationRepository;
import com.example.weather_api.Repository.WeatherApiRepository;
import com.example.weather_api.Service.WeatherApiService;
import com.example.weather_api.dto.WeatherDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class WeatherApiApplicationTests {

	@Mock
	WeatherApiRepository weatherApiRepository;

	@Mock
	LocationRepository locationRepository;

	@Mock
	RestTemplate restTemplate;

	@InjectMocks
	WeatherApiService weatherApiService;

	@Test
	public void getWeatherInfo_WhenDataIsAlreadyPresent_ShouldReturnFromDatabase() throws Exception {

		// Given
		String pincode = "411014";
		LocalDate forDate = LocalDate.of(2020, 10, 15);

		// Mock Location object
		Location location = new Location();
		location.setPincode(pincode);
		location.setLatitude(18.5204);
		location.setLongitude(73.8567);

		// Mock WeatherInfo object
		WeatherInfo weatherInfo = new WeatherInfo();
		weatherInfo.setDate(forDate);
		WeatherDetails weatherDetails = new WeatherDetails("Clear sky", 298.15, 70, 3.5);
		weatherInfo.setWeatherDetails(weatherDetails);
		weatherInfo.setLocation(location);

		// Mock weatherInfoList
		List<WeatherInfo> weatherInfoList = new ArrayList<>();
		weatherInfoList.add(weatherInfo);
		location.setWeatherInfoList(weatherInfoList);

		// Mock the repository behavior
		Mockito.when(locationRepository.findByPincode(pincode)).thenReturn(Optional.of(location));

		// When
		WeatherInfo result = weatherApiService.getWeatherInfo(pincode, forDate);

		// Then
		Assertions.assertEquals(weatherInfo, result);
	}

	@Test
	public void getWeatherInfo_WhenDataIsNotPresent_ShouldFetchFromAPI() throws Exception {
		// Given
		String pincode = "411014";
		LocalDate forDate = LocalDate.of(2020, 10, 15);

		// Mock the location repository to return no location initially
		Mockito.when(locationRepository.findByPincode(pincode)).thenReturn(Optional.empty());

		// Mock the response from the geocoding API with valid lat/lon fields
		String geoApiResponse = "{\"lat\":18.5204,\"lon\":73.8567}";
		Mockito.when(restTemplate.getForObject(Mockito.contains("geo/1.0/zip"), Mockito.eq(String.class)))
				.thenReturn(geoApiResponse);

		// Mock the weather API response
		String weatherApiResponse = "{\"weather\":[{\"description\":\"Clear sky\"}],\"main\":{\"temp\":298.15,\"humidity\":70},\"wind\":{\"speed\":3.5}}";
		Mockito.when(restTemplate.getForObject(Mockito.contains("data/2.5/weather"), Mockito.eq(String.class)))
				.thenReturn(weatherApiResponse);

		// When
		WeatherInfo result = weatherApiService.getWeatherInfo(pincode, forDate);

		// Creating the expected WeatherInfo object for assertions
		WeatherDetails expectedWeatherDetails = new WeatherDetails("Clear sky", 298.15, 70, 3.5);
		WeatherInfo expectedWeatherInfo = new WeatherInfo();
		expectedWeatherInfo.setDate(forDate);
		expectedWeatherInfo.setWeatherDetails(expectedWeatherDetails);
		expectedWeatherInfo.setLocation(new Location(0, pincode, 18.5204, 73.8567, new ArrayList<>()));

		// Then
		Assertions.assertEquals(expectedWeatherInfo.getWeatherDetails(), result.getWeatherDetails());
		Assertions.assertEquals(expectedWeatherInfo.getDate(), result.getDate());
		Assertions.assertEquals(expectedWeatherInfo.getLocation().getPincode(), result.getLocation().getPincode());
	}
}