package com.anshika.weather_assistant_mcp;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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
        String response = restClient.get()
                .uri("/points/38.8894,-77.0352")
                .retrieve()
                .body(String.class);
        return response;
    }
}
