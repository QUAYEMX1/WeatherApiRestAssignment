# 🌦️Weather Information API

## Overview

**The Weather Information API**provides users with weather data based on a given pincode and date. It retrieves data from an external weather service (OpenWeatherMap) and caches results in a relational database for improved efficiency. This API allows users to obtain detailed weather information, including temperature, humidity, and wind speed.
## ✨Features
- Retrieve weather information based on pincode and date.
- Cache weather data to avoid redundant API calls.
- Use of external geocoding service to convert pincode to latitude and longitude.
- Detailed weather details returned in the response.

## Project Structure

### 🛠️Backend

- **Framework**: Spring Boot
- **Language**: Java
- **Development Environment**: IntelliJ IDEA

  ## Technologies Used

- **Java**: The programming language used for developing the API.
- **Spring Boot**: A framework that simplifies the development of Java applications and provides a robust environment for building RESTful services.
- **JPA/Hibernate**: Used for object-relational mapping (ORM) to manage database interactions.
- **MySQL**: The relational database management system used to store weather data and locations.
- **Lombok**: A library that helps reduce boilerplate code in Java by automatically generating getters, setters, and other common methods.
- **OpenWeatherMap API**: The external service used to fetch real-time weather data based on geographical coordinates.
- **RESTful API Design**: The architectural style used to create scalable and stateless web services.
- **JUnit**: A testing framework used for writing and running unit tests to ensure code quality.

## Installation

### Prerequisites

- Java JDK 11 or higher
- Maven
- MySQL Database
- **An OpenWeatherMap API key** (sign up at [OpenWeatherMap](https://openweathermap.org/api) to get an API key).
- i have Provided  my API Key in Application.Properties, you can use the same to run the Application 
 src/main/resources/application.properties.
  - **Application.Properties=:**
  openweathermap.api.key=YOU WILL GET MY API KEY HERE

### Clone the Repository

To get started, clone the repository with the following commands:

```bash
git clone https://github.com/QUAYEMX1/WeatherApiRestAssignment.git
cd WeatherApiRestAssignment
