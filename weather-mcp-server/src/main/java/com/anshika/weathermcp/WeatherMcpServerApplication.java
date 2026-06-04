package com.anshika.weathermcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherMcpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherMcpServerApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider mcpToolProvider(
			GreetingTools greetingTools){
		return MethodToolCallbackProvider.builder()
				.toolObjects(greetingTools)
				.build();
	}
}
