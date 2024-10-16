package com.example.weather_api.Service;

import com.example.weather_api.Models.Location;
import com.example.weather_api.Models.WeatherInfo;
import com.example.weather_api.Repository.LocationRepository;
import com.example.weather_api.Repository.WeatherApiRepository;
import com.example.weather_api.dto.WeatherDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherApiService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    WeatherApiRepository weatherApiRepository;
    @Autowired
    LocationRepository locationRepository;
    @Value("${openweather.api.key}")
    String openWeatherApiKey;

    private final String geoUrl = "https://api.openweathermap.org/geo/1.0/zip?zip=";
    private final String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=";

    public WeatherInfo getWeatherInfo(String pincode, LocalDate forDate) throws Exception {

        // Step 1: Check if lat/lon data for the pincode exists in the database
        Location location = null;
        Optional<Location> optionalLocation = locationRepository.findByPincode(pincode);
        if (optionalLocation.isPresent()) {
               location = optionalLocation.get();
        } else {
            // Fetch lat/lon from Geocoding API
                JsonNode pin = getLatLonFromPincode(pincode);

            // Check if "lat" and "lon" are present in the response
            if (pin.has("lat") && pin.has("lon")) {
                double lat = pin.get("lat").asDouble();
                double lon = pin.get("lon").asDouble();

                // Save new pincode-location mapping in the database
                location = new Location();
                location.setPincode(pincode);
                location.setLatitude(lat);
                location.setLongitude(lon);

                locationRepository.save(location);
            } else {
                // Handle the case where lat/lon data is not found
                throw new Exception("Geocoding API did not return valid lat/lon data for pincode: " + pincode);
            }
        }

        // Step 2: Check if weather data for the pincode and date is already in the database
        List<WeatherInfo> weatherInfoList = location.getWeatherInfoList();
        for (WeatherInfo weatherInfo1 : weatherInfoList) {
            if (weatherInfo1.getDate().isEqual(forDate)) {
                return weatherInfo1;
            }
        }

        WeatherDetails weatherDesc = RetrieveWeatherData(location.getLatitude(), location.getLongitude());

        WeatherInfo obj = new WeatherInfo();
        obj.setDate(forDate);
        obj.setWeatherDetails(weatherDesc);
        obj.setLocation(location);

        // Set the bi-directional relationship
        location.getWeatherInfoList().add(obj);

        // Save both WeatherInfo and Location entities
        weatherApiRepository.save(obj);
        locationRepository.save(location);

        return obj; // Return the weather information object
    }


    private WeatherDetails RetrieveWeatherData(double latitude, double longitude)throws Exception {
        String baseUrl=weatherUrl + latitude +"&lon="+ longitude+"&appid="+openWeatherApiKey;
        String response=restTemplate.getForObject(baseUrl,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode res = objectMapper.readTree(response);

        // Extract required fields from the response
        WeatherDetails weatherDetails = new WeatherDetails();
        weatherDetails.setWeatherDescription(res.get("weather").get(0).get("description").asText());
        weatherDetails.setTemperature(res.get("main").get("temp").asDouble());
        weatherDetails.setHumidity(res.get("main").get("humidity").asInt());
        weatherDetails.setWindSpeed(res.get("wind").get("speed").asDouble());

        return weatherDetails;
    }

    public JsonNode getLatLonFromPincode(String pincode) throws Exception{
        String url=prepareUrl(pincode);
        String res=restTemplate.getForObject(url,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(res);
    }
    public String prepareUrl(String pincode){
        String res=geoUrl+pincode+",IN&appid="+openWeatherApiKey;
        return res;
    }
}
