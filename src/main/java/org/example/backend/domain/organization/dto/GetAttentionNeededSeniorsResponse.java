package org.example.backend.domain.organization.dto;

import java.util.List;

public record GetAttentionNeededSeniorsResponse(
        List<AlertDto> alerts
) {
    public record AlertDto(
            Long recordId,
            String date,
            String seniorName,
            String volunteerName,
            String status,
            String duration
    ) {}
} 