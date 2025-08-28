package org.example.backend.domain.organization.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record GetOrganziationMainResponse(
        WeeklyVolunteerStatus weeklyVolunteerStatus,
        List<VolunteerNeedingAttention> volunteersNeedingAttention,
        List<SeniorStatus> seniorStatuses
) {
    public record WeeklyVolunteerStatus(
            int progressRate,
            int totalCount,
            int completedCount,
            int absentCount,
            int missedCount,
            int pendingCount
    ) {}

    public record VolunteerNeedingAttention(
            LocalDate date,
            String seniorName,
            String status
    ) {}

    public record SeniorStatus(
            Long seniorId,
            String name,
            int age,
            String volunteerName,
            LocalDateTime nextSchedule,
            MonthlyCalls monthlyCalls
    ) {
        public record MonthlyCalls(
                int completed,
                int target
        ) {}
    }
}
