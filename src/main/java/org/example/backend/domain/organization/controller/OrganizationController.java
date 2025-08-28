package org.example.backend.domain.organization.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.organization.dto.RegisterOrganizationRequest;
import org.example.backend.domain.organization.service.RegisterOrganizationService;
import org.example.backend.global.common.response.BaseResponse;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.example.backend.global.swagger.SwaggerResponseDescription;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.backend.global.swagger.SwaggerResponseDescription.REGISTER;

@Tag(
        name = "Organization",
        description = "기관 관련 API"
)
@Slf4j
@RequiredArgsConstructor
@RequestMapping("organizations")
@RestController
public class OrganizationController {
    private final RegisterOrganizationService registerOrganizationService;

    @Operation(
            summary = "기관 회원가입 API",
            description = "기관의 회원가입을 수행하는 API"
    )
    @CustomExceptionDescription(REGISTER)
    @PostMapping
    public BaseResponse<Void> register(@RequestBody RegisterOrganizationRequest request){
        registerOrganizationService.register(request);
        return new BaseResponse<>(null);
    }
}
