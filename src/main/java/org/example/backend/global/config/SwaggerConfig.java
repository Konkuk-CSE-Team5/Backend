package org.example.backend.global.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.example.backend.global.common.response.BaseErrorResponse;
import org.example.backend.global.common.response.status.BaseExceptionResponseStatus;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.example.backend.global.swagger.ExampleHolder;
import org.example.backend.global.swagger.SwaggerResponseDescription;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

@OpenAPIDefinition(
        info = @Info(
                title = "온잇 API 명세서",
                description = "Springdoc을 이용한 Swagger API 문서입니다.",
                version = "1.0"
        )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        final String schemeName = "bearerAuth";

        return new OpenAPI()
                // 전역 SecurityRequirement: 아래 스키마를 모든 API에 적용
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                // SecurityScheme 정의: Bearer JWT
                .components(new Components().addSecuritySchemes(
                        schemeName,
                        new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")   // (표시용)
                                .description("Authorization 헤더에 'Bearer {JWT}' 형태로 전달")
                ));
//        return new OpenAPI();
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {

            CustomExceptionDescription customExceptionDescription = handlerMethod.getMethodAnnotation(
                    CustomExceptionDescription.class);

            // CustomExceptionDescription 어노테이션 단 메소드 적용
            if (customExceptionDescription != null) {
                generateErrorCodeResponseExample(operation, customExceptionDescription.value());
            }

            return operation;
        };
    }

    private void generateErrorCodeResponseExample(
            Operation operation, SwaggerResponseDescription type) {

        ApiResponses responses = operation.getResponses();

        Set<BaseExceptionResponseStatus> baseExceptionResponseStatusSet = type.getExceptionResponseStatusSet();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders =
                baseExceptionResponseStatusSet.stream()
                        .map(
                                baseExceptionResponseStatus -> {
                                    return ExampleHolder.builder()
                                            .holder(
                                                    getSwaggerExample(baseExceptionResponseStatus))
                                            .code(baseExceptionResponseStatus.getStatus())
                                            .name(baseExceptionResponseStatus.toString())
                                            .build();
                                }
                        ).collect(groupingBy(ExampleHolder::getCode));
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private Example getSwaggerExample(BaseExceptionResponseStatus status) {
        Example example = new Example();

        BaseErrorResponse errorResponse = new BaseErrorResponse(status);
        example.setValue(errorResponse);
        example.description(status.getMessage());

        return example;
    }

    private void addExamplesToResponses(
            ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();
                    v.forEach(
                            exampleHolder -> {
                                mediaType.addExamples(
                                        exampleHolder.getName(), exampleHolder.getHolder());
                            });
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setDescription("");
                    apiResponse.setContent(content);
                    responses.addApiResponse(status.toString(), apiResponse);
                });
    }
}
