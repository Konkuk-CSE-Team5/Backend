package org.example.backend.domain.senior.dto;

import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.global.util.DurationUtil;
import org.example.backend.global.util.LocalDateTimeUtil;

import java.util.List;
import java.util.stream.Collectors;

public record GetSeniorMatchingRecords (
        String seniorName,
        List<Record> records
){
    public static GetSeniorMatchingRecords from(String seniorName, String volunteerName, List<VolunteerRecord> matchings) {
        List<Record> collect = matchings.stream()
                .map(match -> Record.entityToDto(volunteerName, match))
                .collect(Collectors.toList());
        return new GetSeniorMatchingRecords(seniorName,
                collect);
    }

    public record Record(
            Long recordId,
            String date,
            String volunteerName,
            String status,
            String duration
    ){
        private static Record entityToDto(String volunteerName, VolunteerRecord entity){
            return new Record (
                    entity.getId(),
                    LocalDateTimeUtil.toFormattedString(entity.getCreatedAt()),
                    volunteerName,
                    entity.getVolunteerRecordStatus().name(),
                    DurationUtil.toHHmmss(entity.getTotalCallTime()));
        }
    }
}
