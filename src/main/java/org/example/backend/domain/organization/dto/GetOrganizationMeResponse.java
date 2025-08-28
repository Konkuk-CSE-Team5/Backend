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
                @Schema(description = "아이디", example = "org123")
                String userId,
                @Schema(description = "기관 이름", example = "찾아유 복지관")
                String name,
                @Schema(description = "담당자 이름", example = "김매니저")
                String manager,
                @Schema(description = "담당자 전화번호", example = "010-1111-2222")
                String managerContact
        ) {}
}
