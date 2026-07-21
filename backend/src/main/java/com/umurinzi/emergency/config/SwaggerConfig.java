package com.umurinzi.emergency.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger UI configuration (SDD §5.12 — "generated live via springdoc-openapi").
 * Served at {@code /swagger-ui.html}; raw spec at {@code /v3/api-docs}.
 */
@Configuration
public class SwaggerConfig {

    private static final String BEARER_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI umurinziOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Umurinzi Emergency Safety Alert System API")
                        .description("REST API for the Umurinzi emergency alert system. See docs/SDD.md in the repository for the full design.")
                        .version("v1")
                        .contact(new Contact().name("Umurinzi").email("umurerwaemerance@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME_NAME, new SecurityScheme()
                                .name(BEARER_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
