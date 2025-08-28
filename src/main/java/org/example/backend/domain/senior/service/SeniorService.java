package org.example.backend.domain.senior.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.matching.MatchingRepository;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.repository.VolunteerRecordRepository;
import org.example.backend.domain.senior.dto.GetSeniorMatchingRecords;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.domain.volunteer.repository.VolunteerRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.ENTITY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class SeniorService {
    private final VolunteerRepository volunteerRepository;
    private final MatchingRepository matchingRepository;
    private final VolunteerRecordRepository volunteerRecordRepository;
    public GetSeniorMatchingRecords getCurrentMatchingRecords(Long userId, Long seniorId) {
        log.info("volId = {} senId = {}", userId, seniorId);

        Volunteer volunteer = volunteerRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));

        // 현재 존재하는 매칭 조회
        Matching matching = matchingRepository.findByVolunteerIdAndSeniorIdAndMatchingStatus(volunteer.getId(), seniorId, MatchingStatus.ACTIVE).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));

        String seniorName = matching.getSenior().getName();
        String volunteerName = matching.getVolunteer().getName();

        // 최신순으로 봉사 레코드 조회
        List<VolunteerRecord> matchings = volunteerRecordRepository.findAllByMatchingOrderByIdDesc(matching);

        // 응답 생성
        return GetSeniorMatchingRecords.from(seniorName, volunteerName, matchings);
    }
}
