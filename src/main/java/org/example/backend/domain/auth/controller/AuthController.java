package org.example.backend.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.auth.dto.LoginRequest;
import org.example.backend.domain.auth.dto.LoginResponse;
import org.example.backend.domain.auth.service.AuthService;
import org.example.backend.global.common.response.BaseResponse;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.example.backend.global.swagger.SwaggerResponseDescription;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.backend.global.swagger.SwaggerResponseDescription.LOGIN;

@Tag(
        name = "Auth",
        description = "로그인 관련 API"
)
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "로그인 API",
            description = "봉사자와 기관의 로그인을 수행하는 API"
    )
    @CustomExceptionDescription(LOGIN)
    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request){
        return new BaseResponse<>(authService.login(request));
    }
}
