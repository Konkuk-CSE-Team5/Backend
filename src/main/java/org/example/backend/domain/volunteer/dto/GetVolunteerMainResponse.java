package org.example.backend.domain.volunteer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@Schema(name = "GetVolunteerMainResponse", description = "봉사자 메인 화면 응답")
public record GetVolunteerMainResponse(
        @ArraySchema(arraySchema = @Schema(description = "어르신 목록"), minItems = 0)
        List<SeniorDto> seniors
) {
    @Builder
    @Schema(name = "Senior", description = "어르신 정보")
    public record SeniorDto(
            @Schema(description = "어르신 ID", example = "123")
            Long seniorId,

            @Schema(description = "어르신 성함", example = "김순자")
            String name,

            @Schema(description = "전화번호", example = "010-1234-5678")
            String phone,

            @ArraySchema(arraySchema = @Schema(description = "스케줄 목록"), minItems = 0)
            List<ScheduleDto> schedule,

            @Schema(description = "특이사항", example = "당뇨, 보조기 사용")
            String notes,

            @Schema(description = "다음 일정", example = "2025-08-27")
            @JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate nextSchedule
    ) {}

    @Builder
    @Schema(name = "Schedule", description = "스케줄 정보")
    public record ScheduleDto(
            @Schema(description = "요일", example = "Tuesday")
            String day,

            @Schema(description = "시작 시간", example = "19:00")
            @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,

            @Schema(description = "종료 시간", example = "20:00")
            @JsonFormat(pattern = "HH:mm")
            LocalTime endTime
    ) {}
}
