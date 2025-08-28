package org.example.backend.domain.senior.dto;

import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.global.util.DurationUtil;
import org.example.backend.global.util.LocalDateTimeUtil;

import java.util.List;

public record GetSeniorMatchingRecords (
        Long seniorId,
        String seniorName,
        String volunteerName,
        String matchingStatus,
        SummaryDto summary,
        List<RecordDto> records

){
    public record SummaryDto(
            Long totalCalls,
            String totalDuration
    ){

    }
    public record RecordDto(
            Long recordId,
            String date,
            String status,
            String duration
    ) {
        public static RecordDto entityToDto(VolunteerRecord entity) {
            return new RecordDto(entity.getId(),
                    LocalDateTimeUtil.toFormattedString(entity.getScheduledDate()),
                    entity.getVolunteerRecordStatus().name(),
                    DurationUtil.toHHmmss(entity.getTotalCallTime()));
        }
    }
}
