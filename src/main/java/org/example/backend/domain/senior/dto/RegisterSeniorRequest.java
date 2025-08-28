package org.example.backend.domain.senior.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record RegisterSeniorRequest(
        @NotBlank(message = "이름은 필수입니다")
        String name,
        
        @NotNull(message = "생년월일은 필수입니다")
        LocalDate birthday,
        
        @NotBlank(message = "연락처는 필수입니다")
        String contact,
        
        @NotNull(message = "봉사 시작일은 필수입니다")
        LocalDate startDate,
        
        @NotNull(message = "봉사 종료일은 필수입니다")
        LocalDate endDate,
        
        @NotEmpty(message = "봉사 요일은 최소 1개 이상 선택해야 합니다")
        List<String> workDays,
        
        @NotNull(message = "봉사 시작 시간은 필수입니다")
        LocalTime workStartTime,
        
        @NotNull(message = "봉사 종료 시간은 필수입니다")
        LocalTime workEndTime
) {
}