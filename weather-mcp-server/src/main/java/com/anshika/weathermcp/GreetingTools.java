package com.anshika.weathermcp;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class GreetingTools {

    @Tool(description = "Greets a person")
    public String greet(String name){
        return "Hello " + name;
    }
}
