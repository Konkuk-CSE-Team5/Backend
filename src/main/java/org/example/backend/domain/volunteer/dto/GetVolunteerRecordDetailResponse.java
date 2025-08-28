package org.example.backend.domain.volunteer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.example.backend.domain.record.model.HealthLevel;
import org.example.backend.domain.record.model.MentalityLevel;
import org.example.backend.domain.record.model.VolunteerRecordStatus;
import org.example.backend.global.util.DurationDeserializer;
import org.example.backend.global.util.DurationSerializer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(name = "GetVolunteerRecordDetailResponse", description = "봉사 기록 상세 응답")
public record GetVolunteerRecordDetailResponse(
        @ArraySchema(arraySchema = @Schema(description = "통화 기록 목록"), minItems = 0)
        List<CallHistoryDto> callHistory,

        @Schema(description = "수행 여부", allowableValues = {"COMPLETE", "ABSENT", "NOT_CONDUCTED"})
        VolunteerRecordStatus status,

        @Schema(description = "건강 상태", allowableValues = {"GOOD", "NORMAL", "BAD"})
        HealthLevel health,

        @Schema(description = "심리 상태", allowableValues = {"GOOD", "NORMAL", "BAD"})
        MentalityLevel mentality,

        @Schema(description = "봉사자 의견")
        String opinion
) {
    @Builder
    @Schema(name = "CallHistory", description = "단일 통화 기록")
    public record CallHistoryDto(
            @Schema(description = "통화 시작 시각(ISO-8601)", example = "2025-08-26T19:32:00")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime dateTime,

            @Schema(description = "통화 시간(HH:mm:ss)", example = "00:07:12")
            @JsonDeserialize(using = DurationDeserializer.class)
            @JsonSerialize(using = DurationSerializer.class)
            Duration callTime
    ) {}
}
