package com.example.weather_api.Repository;

import com.example.weather_api.Models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location,Integer> {

    Optional<Location> findByPincode(String pincode);
}
