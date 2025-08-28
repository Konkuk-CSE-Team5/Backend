package org.example.backend.domain.volunteer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import org.example.backend.domain.record.model.HealthLevel;
import org.example.backend.domain.record.model.MentalityLevel;
import org.example.backend.domain.record.model.VolunteerRecordStatus;

@Schema(name = "PatchVolunteerRecordRequest", description = "봉사 기록 수정 요청 바디")
public record PatchVolunteerRecordRequest(
        @Schema(description = "수행 여부",
                allowableValues = {"COMPLETE", "ABSENT", "NOT_CONDUCTED"},
                example = "COMPLETE",
                nullable = true)
        VolunteerRecordStatus status,

        @Schema(description = "건강 상태",
                allowableValues = {"GOOD", "NORMAL", "BAD"},
                example = "GOOD",
                nullable = true)
        HealthLevel health,

        @Schema(description = "심리 상태",
                allowableValues = {"GOOD", "NORMAL", "BAD"},
                example = "GOOD",
                nullable = true)
        MentalityLevel mentality,

        @Size(max = 1000)
        @Schema(description = "봉사자 의견(최대 1000자)",
                example = "오늘따라 기분이 좋아 보이셨습니다. 식사도 잘 하셨고, 어제는 경로당에도 다녀오셨다고 합니다. 특별한 건강문제는 없어보입니다.",
                nullable = true)
        String opinion
) {
}
