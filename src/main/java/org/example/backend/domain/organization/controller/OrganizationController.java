package org.example.backend.domain.organization.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.organization.dto.*;
import org.example.backend.domain.organization.service.GetOrgSeniorsService;
import org.example.backend.domain.organization.service.OrganizationMeService;
import org.example.backend.domain.organization.service.OrganizationMainService;
import org.example.backend.domain.organization.service.RegisterOrganizationService;
import org.example.backend.global.auth.annotation.LoginUserId;
import org.example.backend.global.common.response.BaseResponse;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.springframework.web.bind.annotation.*;

import static org.example.backend.global.swagger.SwaggerResponseDescription.*;

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
    private final OrganizationMainService organizationMainService;
    private final GetOrgSeniorsService getOrgSeniorsService;

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
            summary = "기관 정보 수정",
            description = "기관의 정보를 수정하는 API"
    )
    @CustomExceptionDescription(DEFAULT)
    @PatchMapping("me")
    public BaseResponse<Void> patchOrganizationMe(
            @LoginUserId @Parameter(hidden = true) Long loginUserId,
            @RequestBody @jakarta.validation.Valid PatchOrganizationMeRequest request) {
        organizationMeService.patch(loginUserId, request);
        return new BaseResponse<>(null);
    }

    @Operation(
            summary = "기관 홈화면 조회",
            description = "기관의 홈화면을 조회하는 API"
    )
    @CustomExceptionDescription(MAIN)
    @Parameter(in = ParameterIn.HEADER, required = true, name = "Authorization", description = "API 엑세스 토큰", example = "Bearer asdf1234")
    @GetMapping("/main")
    public BaseResponse<GetOrganziationMainResponse> getMain(@Parameter(hidden = true) @LoginUserId Long userId){
        return new BaseResponse<>(organizationMainService.getMain(userId));
    }

    @Operation(
            summary = "기관 어르신 등록 화면 조회",
            description = "어르신 등록 화면을 조회하는 API"
    )
    @CustomExceptionDescription(MAIN)
    @GetMapping("/me/seniors")
    public BaseResponse<GetOrgSeniorsResponse> getOrgSeniors(@Parameter(hidden = true) @LoginUserId Long orgUserId){
        return new BaseResponse(getOrgSeniorsService.getSeniors(orgUserId));
    }

    @Operation(
            summary = "기관 어르신 관리 화면 조회",
            description = "어르신 관리 화면을 조회하는 API"
    )
    @CustomExceptionDescription(MAIN)
    @GetMapping("/me/seniors/{seniorId}")
    public BaseResponse<GetSeniorManagePageResponse> getSeniorManagementPage(@Parameter(hidden = true) @LoginUserId Long orgUserId,
                                                                             @PathVariable Long seniorId){
        return new BaseResponse<>(getOrgSeniorsService.getSenior(orgUserId, seniorId));
    }
}
