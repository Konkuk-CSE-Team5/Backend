package org.example.backend.domain.volunteer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.backend.domain.volunteer.model.Gender;

import java.time.LocalDate;

@Schema(description = "봉사자 프로필 수정 요청")
public record PatchVolunteerMeRequest(
        @Schema(description = "비밀번호", example = "ikhwan1234")
//        @jakarta.validation.constraints.Size(min = 8, max = 64)
        String password,
        @Schema(description = "이름", example = "홍길동")
        @jakarta.validation.constraints.NotBlank
        String name,
        @Schema(description = "생년월일", example = "2025-01-01")
        @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
        @jakarta.validation.constraints.Past
        LocalDate birthday,
        @Schema(description = "성별", example = "FEMALE")
        Gender gender,
        @Schema(description = "연락처", example = "010-0000-0000" )
        @jakarta.validation.constraints.Pattern(regexp = "^01[016-9]-\\d{3,4}-\\d{4}$")
        String contact
) {
}
