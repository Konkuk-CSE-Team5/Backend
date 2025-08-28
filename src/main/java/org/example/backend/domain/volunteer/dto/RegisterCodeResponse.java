package org.example.backend.domain.volunteer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Schema(name = "RegisterCodeResponse", description = "코드 등록 응답")
public record RegisterCodeResponse(
        @Schema(description = "어르신 성함", example = "김순자")
        String name,

        @ArraySchema(arraySchema = @Schema(description = "스케줄 목록"), minItems = 0)
        List<ScheduleDto> schedule,

        @Schema(description = "특이사항", example = "당뇨, 보조기 사용")
        String notes,

        @Schema(description = "다음 일정", example = "2025-08-27")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("next_schedule")
        LocalDate nextSchedule
) {
    @Builder
    @Schema(name = "Schedule", description = "스케줄 정보")
    public record ScheduleDto(
            @Schema(description = "요일", example = "Tuesday")
            String day,

            @Schema(description = "시간", example = "19:00")
            @JsonFormat(pattern = "HH:mm")
            LocalTime time
    ) {}
}
