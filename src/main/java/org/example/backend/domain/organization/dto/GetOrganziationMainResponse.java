package org.example.backend.domain.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "홈화면 응답")
public record GetOrganziationMainResponse(
        WeeklyVolunteerStatus weeklyVolunteerStatus,
        List<VolunteerNeedingAttention> volunteersNeedingAttention,
        List<SeniorStatus> seniorStatuses
) {
    @Schema(description = "이번주 봉사 현황")
    public record WeeklyVolunteerStatus(
            @Schema(description = "진행도")
            int progressRate,

            @Schema(description = "총 개수")
            int totalCount,
            @Schema(description = "완료")
            int completedCount,
            @Schema(description = "부재중")
            int absentCount,
            @Schema(description = "미실시")
            int missedCount,
            @Schema(description = "예정")
            int pendingCount
    ) {}

    @Schema(description = "주의가 필요한 봉사")
    public record VolunteerNeedingAttention(
            @Schema(description = "날짜")
            LocalDate date,
            @Schema(description = "어르신 이름")
            String seniorName,
            @Schema(description = "상태")
            String status
    ) {}

    @Schema(description = "어르신 현황")
    public record SeniorStatus(
            @Schema(description = "어르신 id")
            Long seniorId,
            @Schema(description = "이름")
            String name,
            @Schema(description = "나이")
            int age,
            @Schema(description = "봉사자 이름")
            String volunteerName,
            @Schema(description = "다음 예정일")
            LocalDateTime nextSchedule,
            @Schema(description = "이번달 예정된 통화")
            MonthlyCalls monthlyCalls
    ) {
        public record MonthlyCalls(
                @Schema(description = "완료 횟수")
                int completed,
                @Schema(description = "예정된 총 횟수")
                int target
        ) {}
    }
}
