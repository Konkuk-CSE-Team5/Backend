package org.example.backend.domain.senior.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.matching.repository.MatchingRepository;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.organization.repository.OrganizationRepository;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.model.VolunteerRecordStatus;
import org.example.backend.domain.record.repository.VolunteerRecordRepository;
import org.example.backend.domain.senior.dto.GetSeniorMatchingRecords;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.senior.repository.SeniorRepository;
import org.example.backend.global.common.exception.CustomException;
import org.example.backend.global.util.DurationUtil;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.ENTITY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrganizationSeniorService {
    private final OrganizationRepository organizationRepository;
    private final SeniorRepository seniorRepository;
    private final MatchingRepository matchingRepository;
    private final VolunteerRecordRepository volunteerRecordRepository;
    public GetSeniorMatchingRecords getCurrentMatchingRecords(Long userId, Long seniorId) {
        log.info("userId = {} senId = {}", userId, seniorId);

        // 기관 조회
        Organization organization = organizationRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));

        Senior senior = seniorRepository.findById(seniorId).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
        if(!senior.getOrganization().equals(organization)){
            throw new CustomException(ENTITY_NOT_FOUND);
        }

        // 현재 존재하는 매칭 조회
        Matching matching = matchingRepository.findBySeniorIdAndMatchingStatus(seniorId, MatchingStatus.ACTIVE).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));

        String seniorName = matching.getSenior().getName();
        String volunteerName = matching.getVolunteer().getName();

        // 최신순으로 봉사 레코드 조회
        List<VolunteerRecord> records = volunteerRecordRepository.findAllByMatchingOrderByIdDesc(matching);

        long totalCalls = records.stream().filter(record -> record.getVolunteerRecordStatus().equals(VolunteerRecordStatus.COMPLETE)).count();
        Duration totalDuration = records.stream().filter(record -> record.getTotalCallTime() != null).map(record -> record.getTotalCallTime()).reduce(Duration.ZERO, Duration::plus);

        List<GetSeniorMatchingRecords.RecordDto> collect = records.stream().map(GetSeniorMatchingRecords.RecordDto::entityToDto).collect(Collectors.toList());

        // 응답 생성
        return new GetSeniorMatchingRecords(seniorId,
                seniorName,
                volunteerName,
                matching.getMatchingStatus().name(),
                new GetSeniorMatchingRecords.SummaryDto(totalCalls, DurationUtil.toHHmmss(totalDuration)),
                collect);
    }
}
