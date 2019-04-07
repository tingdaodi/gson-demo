package com.gson.gsondemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
@EnableConfigurationProperties
public class GsonDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsonDemoApplication.class, args);
    }

}
