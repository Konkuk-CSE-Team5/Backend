package org.example.backend.domain.volunteer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.record.model.VolunteerRecordStatus;
import org.example.backend.global.util.DurationDeserializer;
import org.example.backend.global.util.DurationSerializer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Builder
@Schema(name = "GetVolunteerRecordResponse", description = "봉사 기록 목록 응답")
public record GetVolunteerRecordResponse(
        @ArraySchema(arraySchema = @Schema(description = "어르신 목록"), minItems = 0)
        List<SeniorDto> seniors
) {
    @Builder
    @Schema(name = "Senior", description = "어르신 요약 및 기록")
    public record SeniorDto(
            @Schema(description = "매칭 ID", example = "123")
            Long matchingId,

            @Schema(description = "어르신 이름", example = "김순자")
            String seniorName,

            @Schema(
                    description = "어르신 상태",
                    example = "ACTIVE"
            )
            MatchingStatus status,

            @Schema(description = "통화 요약")
            Summary summary,

            @ArraySchema(arraySchema = @Schema(description = "봉사 기록 목록"), minItems = 0)
            List<Record> records
    ) {}

    @Builder
    @Schema(name = "Summary", description = "어르신 통화 요약")
    public record Summary(
            @Schema(description = "총 통화 횟수", example = "2")
            Integer totalCalls,

            @Schema(description = "총 통화 시간(HH:mm:ss)", example = "00:19:32")
            @JsonDeserialize(using = DurationDeserializer.class)   // 커스텀 역직렬화
            @JsonSerialize(using = DurationSerializer.class)
            Duration totalDuration
    ) {}

    @Builder
    @Schema(name = "Record", description = "개별 봉사 기록")
    public record Record(
            @Schema(description = "기록 ID", example = "1")
            Long recordId,

            @Schema(description = "봉사/통화 일시(ISO-8601)", example = "2025-08-26T19:32:00")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime dateTime,

            @Schema(description = "통화 시간(HH:mm:ss), 미실시/부재중이면 null 또는 00:00:00", example = "00:12:30", nullable = true)
            @JsonDeserialize(using = DurationDeserializer.class)   // 커스텀 역직렬화
            @JsonSerialize(using = DurationSerializer.class)
            Duration duration,

            @Schema(
                    description = "기록 상태",
                    example = "COMPLETE"
            )
            VolunteerRecordStatus status
    ) {}
}
