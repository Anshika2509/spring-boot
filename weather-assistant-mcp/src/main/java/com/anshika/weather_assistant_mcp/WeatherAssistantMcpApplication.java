package com.anshika.weather_assistant_mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherAssistantMcpApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherAssistantMcpApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider weatherTools( WeatherService weatherService){
		return MethodToolCallbackProvider.builder()
				.toolObjects(weatherService)
				.build();
	}

}
