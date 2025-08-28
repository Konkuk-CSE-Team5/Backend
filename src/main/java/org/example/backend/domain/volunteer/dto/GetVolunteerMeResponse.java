package org.example.backend.domain.volunteer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "GetVolunteerMeResponse", description = "봉사자 프로필 조회 응답")
public record GetVolunteerMeResponse(
        @Schema(description = "프로필 정보")
        ProfileGetDto profile
) {
        @Builder
        public record ProfileGetDto(
                @Schema(description = "이름", example = "홍길동")
                String name,
                @Schema(description = "전화번호", example = "010-1234-5678")
                String phone
        ) {}
}
