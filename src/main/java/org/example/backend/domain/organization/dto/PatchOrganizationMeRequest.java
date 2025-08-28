package org.example.backend.domain.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기관 프로필 수정 요청")
public record PatchOrganizationMeRequest(
        @Schema(description = "비밀번호", example = "new_password")
//        @jakarta.validation.constraints.Size(min = 8, max = 64)
        String password,
        @Schema(description = "기관명", example = "뉴 광진복지센터")
        @jakarta.validation.constraints.Size(min = 1, max = 64)
//        @jakarta.validation.constraints.Pattern(regexp = ".*\\S.*")
        String name,
        @Schema(description = "담당자", example = "홍길동")
        @jakarta.validation.constraints.Size(min = 1, max = 64)
//        @jakarta.validation.constraints.Pattern(regexp = ".*\\S.*")
        String manager,
        @Schema(description = "담당자 연락처", example = "010-7777-7777")
//        @jakarta.validation.constraints.Pattern(regexp = "^01[016-9]-\\d{3,4}-\\d{4}$")
        String managerContact
) {
}
