package com.everyones.lawmaking.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("All")
                .pathsToMatch("/v1/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiNeededAuthorization() {
        return GroupedOpenApi.builder()
                .group("Authorization")
                .pathsToMatch("/**/like**", "/**/bookmark**")
                .build();
    }



    private Info apiInfo() {
        return new Info()
                .title("LawDigest V1 API")
                .description("LawBag 명세 문서.")
                .version("1.0.0");
    }
}
