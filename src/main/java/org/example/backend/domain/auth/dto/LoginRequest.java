package org.example.backend.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "아이디", example = "ikhwan")
        String username,
        @Schema(description = "비밀번호", example = "ikhwan1234")
        String password,
        @Schema(description = "유저 타입")
        String role
) {
}
