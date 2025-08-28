package org.example.backend.domain.volunteer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "코드 등록 요청")
public record RegisterCodeRequest(
        @Schema(description = "발급받은 코드", example = "254565")
        @NotNull(message = "코드는 필수입니다")
        String code
) {}
