package org.example.backend.domain.volunteer.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.example.backend.domain.record.model.HealthLevel;
import org.example.backend.domain.record.model.MentalityLevel;
import org.example.backend.domain.record.model.VolunteerRecordStatus;
import org.example.backend.global.util.DurationDeserializer;
import org.example.backend.global.util.DurationSerializer;

import java.time.Duration;
import java.util.List;

@Schema(name = "RecordCreateRequest",
        description = "봉사 통화 기록 생성 요청 바디")
public record PostVolunteerRecordRequest(
        @NotNull
        @Schema(description = "어르신 식별자", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
        Long seniorId,

        @NotEmpty
        @Valid
        @Schema(description = "통화 이력 목록", requiredMode = Schema.RequiredMode.REQUIRED)
        List<CallHistory> callHistory,

        @NotNull
        @Schema(description = "수행 여부",
                allowableValues = {"COMPLETE", "ABSENT"},
                example = "COMPLETE",
                requiredMode = Schema.RequiredMode.REQUIRED)
        VolunteerRecordStatus status,

        @NotNull
        @Schema(description = "건강 상태",
                allowableValues = {"GOOD", "NORMAL", "BAD"},
                example = "GOOD",
                requiredMode = Schema.RequiredMode.REQUIRED)
        HealthLevel health,

        @NotNull
        @Schema(description = "심리 상태",
                allowableValues = {"GOOD", "NORMAL", "BAD"},
                example = "GOOD",
                requiredMode = Schema.RequiredMode.REQUIRED)
        MentalityLevel mentality,

        @Size(max = 1000)
        @Schema(description = "봉사자 의견(최대 1000자)",
                example = "오늘따라 기분이 좋아 보이셨습니다. 식사도 잘 하셨고, 어제는 경로당에도 다녀오셨다고 합니다. 특별한 건강문제는 없어보입니다.")
        String opinion
) {

    @Schema(name = "RecordCreateRequest.CallHistory",
            description = "단일 통화 이력")
    public record CallHistory(
            @NotNull
            @Schema(description = "통화 시각",
                    type = "string",
                    format = "date-time",
                    example = "2025-08-26T19:32:00",
                    requiredMode = Schema.RequiredMode.REQUIRED)
            java.time.LocalDateTime dateTime,

            @NotNull
            @Schema(description = "통화 소요 시간 (HH:mm:ss)",
                    example = "00:07:12",
                    requiredMode = Schema.RequiredMode.REQUIRED)
            @JsonDeserialize(using = DurationDeserializer.class)   // 커스텀 역직렬화
            @JsonSerialize(using = DurationSerializer.class)       // 커스텀 직렬화
            Duration callTime
    ) {}
}