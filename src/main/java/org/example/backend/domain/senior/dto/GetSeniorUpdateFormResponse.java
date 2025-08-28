package org.example.backend.domain.senior.dto;

import java.time.LocalDate;
import java.util.List;

public record GetSeniorUpdateFormResponse(
        String name,
        LocalDate birthday,
        String contact,
        LocalDate startDate,
        LocalDate endDate,
        List<ScheduleDto> schedule,
        String notes
) {
    public record ScheduleDto(
            String day,
            String startTime,
            String endTime
    ) {}
} 