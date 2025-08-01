package com.foodon.foodon.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(securityComponents())
                .addSecurityItem(securityItem())
                .info(apiInfo());
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("All")
                .pathsToMatch("/**")
                .packagesToScan("com.foodon.foodon")
                .build();
    }

    private Components securityComponents() {
        return new Components()
                .addSecuritySchemes("bearer-key",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }

    private SecurityRequirement securityItem() {
        return new SecurityRequirement()
                .addList("bearer-key"); // 위 이름과 같아야 함
    }


    private Info apiInfo() {
        return new Info()
                .title("S203 - 푸드온 API 명세")
                .description("S203 기업연계 프로젝트 푸드온 API 명세입니다.")
                .version("1.0.0");
    }

}
