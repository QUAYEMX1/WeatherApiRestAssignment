package com.example.weather_api.Service;

import com.example.weather_api.Models.Location;
import com.example.weather_api.Models.weatherInfo;
import com.example.weather_api.Repository.LocationRepository;
import com.example.weather_api.Repository.WeatherApiRepository;
import com.example.weather_api.dto.WeatherDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
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

    public weatherInfo getWeatherInfo(String pincode, LocalDate forDate) throws Exception{
        // Step 1: Check if weather data for the pincode and date is already in the database
        Optional<weatherInfo> optionalWeatherInfo=weatherApiRepository.findByPincodeAndDate(pincode, forDate);
        if(optionalWeatherInfo.isPresent()){
            return optionalWeatherInfo.get();
        }

        // Step 2: Check if lat/lon data for the pincode exists in the database
        Location pincodeLocation=null;
        Optional<Location>optionalLocation=locationRepository.findByPincode(pincode);
        if(optionalLocation.isPresent()){
            pincodeLocation=optionalLocation.get();
        }else{
            // Fetch lat/lon from Geocoding API
            JsonNode pin=getLatLonFromPincode(pincode);
            double lat = pin.get("lat").asDouble();
            double lon = pin.get("lon").asDouble();

            // Save new pincode-location mapping in the database
            pincodeLocation=new Location();
            pincodeLocation.setPincode(pincode);
            pincodeLocation.setLatitude(lat);
            pincodeLocation.setLongitude(lon);

            locationRepository.save(pincodeLocation);
        }

        WeatherDetails weatherDesc=RetrieveWeatherData(pincodeLocation.getLatitude(),pincodeLocation.getLongitude());

        weatherInfo obj=new weatherInfo();
        obj.setPincode(pincode);
        obj.setDate(forDate);
        obj.setWeatherDetails(weatherDesc);

        weatherApiRepository.save(obj);

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
