package com.darshan.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI ecomOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Backend API")
                        .description("API documentation for E-Commerce Application (Backend Only)")
                        .version("v1.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("Project GitHub Repo")
                        .url("https://github.com/your-repo-link"));
    }
}
//http://localhost:8080/swagger-ui/index.html