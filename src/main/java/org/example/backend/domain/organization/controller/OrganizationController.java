package org.example.backend.domain.organization.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.organization.dto.GetOrganizationMeResponse;
import org.example.backend.domain.organization.dto.PatchOrganizationMeRequest;
import org.example.backend.domain.organization.dto.RegisterOrganizationRequest;
import org.example.backend.domain.organization.service.OrganizationMeService;
import org.example.backend.domain.organization.service.RegisterOrganizationService;
import org.example.backend.domain.volunteer.dto.GetVolunteerMeResponse;
import org.example.backend.domain.volunteer.dto.PatchVolunteerMeRequest;
import org.example.backend.global.auth.annotation.LoginUserId;
import org.example.backend.global.common.response.BaseResponse;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.example.backend.global.swagger.SwaggerResponseDescription;
import org.springframework.web.bind.annotation.*;

import static org.example.backend.global.swagger.SwaggerResponseDescription.DEFAULT;
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
    private final OrganizationMeService organizationMeService;

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

    @Operation(
            summary = "기관 설정화면 조회",
            description = "기관의 설정화면 정보를 조회하는 API"
    )
    @CustomExceptionDescription(DEFAULT)
    @GetMapping("me")
    public BaseResponse<GetOrganizationMeResponse> getOrganizationMe(@LoginUserId @Parameter(hidden = true) Long loginUserId) {
        return new BaseResponse<>(organizationMeService.get(loginUserId));
    }

    @Operation(
            summary = "기관 설정화면 조회",
            description = "기관의 설정화면 정보를 조회하는 API"
    )
    @CustomExceptionDescription(DEFAULT)
    @PatchMapping("me")
    public BaseResponse<Void> patchOrganizationMe(
            @LoginUserId @Parameter(hidden = true) Long loginUserId,
            @RequestBody @jakarta.validation.Valid PatchOrganizationMeRequest request) {
        organizationMeService.patch(loginUserId, request);
        return new BaseResponse<>(null);
    }
}
