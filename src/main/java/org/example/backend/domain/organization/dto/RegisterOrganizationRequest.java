package org.example.backend.domain.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기관 회원가입 요청")
public record RegisterOrganizationRequest(
        @Schema(description = "아이디", example = "ikhwan")
        String username,
        @Schema(description = "비밀번호", example = "ikhwan1234")
        String password,
        @Schema(description = "기관명", example = "광진노인복지센터")
        String name,
        @Schema(description = "담당자명", example = "홍길동")
        String manager,
        @Schema(description = "담당자연락처", example = "010-0000-0000")
        String managerContact
) {
}
