package org.example.backend.domain.senior.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "어르신 정보 수정 요청")
public record PatchSeniorRequest(
        @Schema(description = "어르신 이름", example = "김춘자", nullable = true)
        String name,
        
        @Schema(description = "생년월일", example = "2001-01-01", nullable = true)
        LocalDate birthday,
        
        @Schema(description = "연락처", example = "010-1234-5678", nullable = true)
        String contact,
        
        @Schema(description = "봉사 시작일", example = "2025-01-01", nullable = true)
        LocalDate startDate,
        
        @Schema(description = "봉사 종료일", example = "2025-01-30", nullable = true)
        LocalDate endDate
) {}
