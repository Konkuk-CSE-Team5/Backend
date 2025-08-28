package org.example.backend.domain.organization.dto;

public record GetSeniorManagePageResponse(
        SeniorProfile seniorProfile,
        String seniorCode,
        MatchedVolunteer matchedVolunteer
) {
    public record SeniorProfile(
            String name,       // 성함
            String birthday,   // 생년월일
            String contact,    // 연락처
            String startDate,  // 봉사 시작 날짜
            String endDate     // 봉사 종료 날짜
    ) {}

    public record MatchedVolunteer(
            String name,       // 봉사자 이름
            String lastWorkDay // 봉사자 최근 활동
    ) {}
}
