package org.koreait.global.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info=@Info(title="포켓몬 도감 API", description = "/api/file - 파일 API"))
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi openApiGroup() {
        return GroupedOpenApi.builder()
                .group("포켓몬 도감 API")
                .pathsToMatch("/api/**")
                .build();
    }
}
