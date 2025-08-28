package org.example.backend.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record LoginResponse(
        @Schema(description = "엑세스 토큰", example = "asdf1234asdf1234")
        String accessToken
) {
}
