package com.example.weather_api;

import com.example.weather_api.Models.Location;
import com.example.weather_api.Models.weatherInfo;
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

		weatherInfo obj = new weatherInfo();
		obj.setPincode(pincode);
		obj.setDate(forDate);
		WeatherDetails weatherDetails = new WeatherDetails("Clear sky", 298.15, 70, 3.5);
		obj.setWeatherDetails(weatherDetails);

		// Mocking the repository behavior
		Mockito.when(weatherApiRepository.findByPincodeAndDate(pincode, forDate)).thenReturn(Optional.of(obj));

		// When
		weatherInfo result = weatherApiService.getWeatherInfo(pincode, forDate);

		Assertions.assertEquals(obj, result);
	}


	//if DataIsNotPresentInDataBase
   @Test
   public void getWeatherInfo_WhenDataIsNotPresent_ShouldFetchFromAPI() throws Exception {
	   // Given
	   String pincode = "411014";
	   LocalDate forDate = LocalDate.of(2020, 10, 15);

	   // Mock the repository to return empty initially
	   Mockito.when(weatherApiRepository.findByPincodeAndDate(pincode, forDate)).thenReturn(Optional.empty());

	   // Mock the location repository to return no location initially
	   Mockito.when(locationRepository.findByPincode(pincode)).thenReturn(Optional.empty());

	   // Mock the response from the geocoding API
	   String geoApiResponse = "{\"lat\":18.5204,\"lon\":73.8567}";
	   Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(geoApiResponse);

	   // Mock the weather API response
	   String weatherApiResponse = "{\"weather\":[{\"description\":\"Clear sky\"}],\"main\":{\"temp\":298.15,\"humidity\":70},\"wind\":{\"speed\":3.5}}";
	   Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(weatherApiResponse);

	   // When
	   weatherInfo result = weatherApiService.getWeatherInfo(pincode, forDate);

	   // Creating the expected weatherInfo object for assertions
	   weatherInfo expectedResult = new weatherInfo();
	   expectedResult.setPincode(pincode);
	   expectedResult.setDate(forDate);
	   WeatherDetails expectedWeatherDetails = new WeatherDetails("Clear sky", 298.15, 70, 3.5);
	   expectedResult.setWeatherDetails(expectedWeatherDetails);

	   Assertions.assertEquals(expectedResult, result);
   }
}
