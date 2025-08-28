package org.example.backend.domain.senior.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.senior.dto.GetSeniorMatchingRecords;
import org.example.backend.domain.senior.dto.RegisterSeniorRequest;
import org.example.backend.domain.senior.dto.RegisterSeniorResponse;
import org.example.backend.domain.senior.service.OrganizationSeniorService;
import org.example.backend.domain.senior.service.RegisterSeniorService;
import org.example.backend.global.auth.annotation.LoginUserId;
import org.example.backend.global.common.response.BaseResponse;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.example.backend.global.swagger.SwaggerResponseDescription;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(
        name = "Senior",
        description = "어르신 관련 API"
)
@Slf4j
@RequiredArgsConstructor
@RequestMapping("seniors")
@RestController
public class SeniorController {
    private final OrganizationSeniorService seniorService;
    private final RegisterSeniorService registerSeniorService;

    // 기관 어르신 활동 기록 조회

    @Operation(
            summary = "기관 어르신 현황 조회",
            description = "기관의 어르신 현황을 조회하는 API"
    )
    @CustomExceptionDescription(SwaggerResponseDescription.MAIN)
    @Parameter(in = ParameterIn.HEADER, required = true, name = "Authorization", description = "API 엑세스 토큰", example = "Bearer asdf1234")
    @GetMapping("/{seniorId}/records")
    public BaseResponse<GetSeniorMatchingRecords> getCurrentMatchingRecords(@LoginUserId Long userId, @PathVariable Long seniorId){
        return new BaseResponse<>(seniorService.getCurrentMatchingRecords(userId, seniorId));
    }
    
    @Operation(
            summary = "어르신 등록 API",
            description = "기관에서 어르신을 등록하는 API"
    )
    @CustomExceptionDescription(SwaggerResponseDescription.REGISTER)
    @PostMapping
    public BaseResponse<RegisterSeniorResponse> registerSenior(
            @LoginUserId @Parameter(hidden = true) Long loginUserId,
            @RequestBody @Valid RegisterSeniorRequest request) {
        return new BaseResponse<>(registerSeniorService.register(loginUserId, request));
    }
}
