package com.everyones.lawmaking.global.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateUtil {
    @Bean
    public RestTemplate restTemplate()  {
        return new RestTemplate();
    }
}
