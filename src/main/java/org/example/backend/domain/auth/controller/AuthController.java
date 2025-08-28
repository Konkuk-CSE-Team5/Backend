package org.example.backend.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.auth.dto.LoginRequest;
import org.example.backend.domain.auth.dto.LoginResponse;
import org.example.backend.domain.auth.service.AuthService;
import org.example.backend.global.common.response.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request){
        return new BaseResponse<>(authService.login(request));
    }
}
