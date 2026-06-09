package com.anshika.weather_assistant_mcp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @GetMapping("/weather-test/{latitude}/{longitude}")
    public String weatherTest(@PathVariable double latitude,
                              @PathVariable double longitude){
        return weatherService.getWeatherForecast(latitude, longitude);
    }

}
