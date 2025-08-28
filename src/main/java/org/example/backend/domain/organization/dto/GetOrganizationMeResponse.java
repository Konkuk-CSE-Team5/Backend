package org.example.backend.domain.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "GetOrganizationMeResponse", description = "기관 프로필 조회 응답")
public record GetOrganizationMeResponse(
        @Schema(description = "프로필 정보")
        ProfileGetDto profile
) {
        @Builder
        public record ProfileGetDto(
                @Schema(description = "기관 이름", example = "행복 복지센터")
                String name,
                @Schema(description = "담당자 이름", example = "홍길동")
                String manager,
                @Schema(description = "전화번호", example = "010-1234-5678")
                String managerContact
        ) {}
}
