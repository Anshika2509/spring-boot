package com.anshika.weather_assistant_mcp;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
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

        Map forecastResponse = restClient.get()
                .uri(forecastUrl)
                .retrieve()
                .body(Map.class);

        Map forecastProperties = (Map) forecastResponse.get("properties");
        List periods = (List) forecastProperties.get("periods");
        Map firstPeriod = (Map) periods.get(0);

        String name = firstPeriod.get("name").toString();
        String temperature = (firstPeriod.get("temperature").toString()) + "°" + firstPeriod.get("temperatureUnit").toString();
        String forecast = firstPeriod.get("detailedForecast").toString();

        System.out.println("""
        %s
        Temperature: %s

        %s
        """.formatted(
                name,
                temperature,
                forecast
        ));

        return """
        <h2>%s</h2>
        <p>Temperature: %s</p>
        <p>%s</p>
        """.formatted(
                name,
                temperature,
                forecast
        );

    }
}
