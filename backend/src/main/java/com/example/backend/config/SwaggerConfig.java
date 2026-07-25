package com.example.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("圖書借閱系統 API")
                        .version("1.0")
                        .description("面試專案的後端 RESTful API 測試與文件"))
                // 告訴 Swagger 我們的 API 需要名為 bearerAuth 的認證
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                // 定義 bearerAuth 是什麼 (這裡指定為 HTTP 的 Bearer Token)
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
