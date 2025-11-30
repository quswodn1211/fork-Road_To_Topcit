package com.opsw.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "LearnTC API",
                version = "v1",
                description = "TOPCIT 스토리 모드와 학습 진행을 위한 백엔드 API 문서"
        )
)
public class OpenApiConfig {
}
