package com.anshika.weather_assistant_mcp;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class WeatherService {
    private final RestClient restClient;

    public WeatherService(){
        System.out.println("Weather Service created");
        this.restClient = RestClient.builder()
                .baseUrl("https://api.weather.gov")
                .defaultHeader("Accept", "application/geo+json")
                .build();
    }

    public String testWeatherApi(){
        Map response = restClient.get()
            .uri("/points/38.8894,-77.0352")
            .retrieve()
            .body(Map.class);
        Map properties = (Map) response.get("properties");
        String forecastUrl = properties.get("forecast").toString();
        return forecastUrl;
    }
}
