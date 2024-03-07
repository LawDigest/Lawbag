package com.everyones.lawmaking.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        var corsConfigSource = new UrlBasedCorsConfigurationSource();

        var corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowedMethods(List.of("*"));
        corsConfig.setAllowedOrigins(List.of("http://localhost:3000", "http://52.79.63.140:3000"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }

}