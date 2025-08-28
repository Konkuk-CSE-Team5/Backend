package org.example.backend.domain.volunteer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "봉사자 회원가입 요청")
public record RegisterVolunteerRequest(
        @Schema(description = "아이디", example = "ikhwan")
        String username,
        @Schema(description = "비밀번호", example = "ikhwan1234")
        String password,
        @Schema(description = "이름", example = "홍길동")
        String name,
        @Schema(description = "생년월일", example = "2025-01-01")
        LocalDate birthday,
        @Schema(description = "성별", example = "female")
        String gender,
        @Schema(description = "연락처", example = "010-0000-0000" )
        String contact
) {
}
