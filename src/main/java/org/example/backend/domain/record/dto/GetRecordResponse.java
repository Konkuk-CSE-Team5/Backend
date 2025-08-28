package org.example.backend.domain.record.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GetRecordResponse(
        Long recordId,
        String status,
        String seniorName,
        String volunteerName,
        LocalDateTime callDateTime,
        String totalCallTime,
        String health,
        String metality,
        String opinion
) {
}
