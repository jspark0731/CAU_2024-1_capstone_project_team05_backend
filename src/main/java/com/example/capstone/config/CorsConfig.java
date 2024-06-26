package com.example.capstone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
        @Bean
        public WebMvcConfigurer corsConfigurer()
        {
                return new WebMvcConfigurer()
                {
                        @Override
                        public void addCorsMappings(CorsRegistry registry)
                        {
                                registry.addMapping("/**")
                                        .allowedOrigins("http://localhost:3000", "http://machine-learning:5000") // frontend app hosted, machine-learning container hosted
                                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                                        .allowedHeaders("*")
                                        .allowCredentials(true);
                        }
                };
        }
}
