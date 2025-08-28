package org.example.backend.domain.record.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.record.dto.GetRecordResponse;
import org.example.backend.domain.record.model.Report;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.model.VolunteerRecordStatus;
import org.example.backend.domain.record.repository.VolunteerRecordRepository;
import org.example.backend.global.common.exception.CustomException;
import org.example.backend.global.util.DurationUtil;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.ENTITY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class RetrieveVolunteerRecordService {
    private final VolunteerRecordRepository volunteerRecordRepository;

    public GetRecordResponse retrieve(Long recordId) {
        // 봉사 기록 조회
        VolunteerRecord volunteerRecord = volunteerRecordRepository.findById(recordId).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));

        // 리포트 조회하여
        // 조합하여 DTO 생성
        VolunteerRecordStatus status = volunteerRecord.getVolunteerRecordStatus();
        if(status.equals(VolunteerRecordStatus.COMPLETE)){
            Report report = volunteerRecord.getReport();
            return new GetRecordResponse(volunteerRecord.getId(),
                    status.name(),
                    volunteerRecord.getMatching().getSenior().getName(),
                    volunteerRecord.getMatching().getVolunteer().getName(),
                    volunteerRecord.getStartTime(),
                    DurationUtil.toHHmmss(volunteerRecord.getTotalCallTime()),
                    report.getHealth().name(),
                    report.getMentality().name(),
                    report.getOpinion());
        }
        if(status.equals(VolunteerRecordStatus.NOT_CONDUCTED)){
            return GetRecordResponse.builder()
                    .recordId(volunteerRecord.getId())
                    .status(status.name())
                    .seniorName(volunteerRecord.getMatching().getSenior().getName())
                    .volunteerName(volunteerRecord.getMatching().getVolunteer().getName())
                    .build();
        }
        return GetRecordResponse.builder()
                .recordId(volunteerRecord.getId())
                .status(status.name())
                .seniorName(volunteerRecord.getMatching().getSenior().getName())
                .volunteerName(volunteerRecord.getMatching().getVolunteer().getName())
                .callDateTime(volunteerRecord.getStartTime())
                .totalCallTime(DurationUtil.toHHmmss(volunteerRecord.getTotalCallTime()))
                .build();
    }
}
