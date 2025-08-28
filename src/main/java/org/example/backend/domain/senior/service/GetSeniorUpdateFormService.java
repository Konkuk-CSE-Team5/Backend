package org.example.backend.domain.senior.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.matching.repository.MatchingRepository;
import org.example.backend.domain.schedule.model.Schedule;
import org.example.backend.domain.schedule.model.ScheduleDetail;
import org.example.backend.domain.schedule.repository.ScheduleDetailRepository;
import org.example.backend.domain.schedule.repository.ScheduleRepository;
import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.organization.repository.OrganizationRepository;
import org.example.backend.domain.senior.dto.GetSeniorUpdateFormResponse;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.senior.repository.SeniorRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.ENTITY_NOT_FOUND;
import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.NOT_FOUND_USER;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetSeniorUpdateFormService {
    private final SeniorRepository seniorRepository;
    private final OrganizationRepository organizationRepository;
    private final MatchingRepository matchingRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    
    public GetSeniorUpdateFormResponse getSeniorUpdateForm(Long organizationUserId, Long seniorId) {
        log.info("[getSeniorUpdateForm] organizationUserId = {}, seniorId = {}", organizationUserId, seniorId);
        
        // 기관 정보 조회
        Organization organization = organizationRepository.findByUserId(organizationUserId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        
        // 어르신 정보 조회
        Senior senior = seniorRepository.findById(seniorId)
                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
        
        // 어르신이 해당 기관에 속하는지 검증
        if (!senior.getOrganization().equals(organization)) {
            throw new CustomException(ENTITY_NOT_FOUND);
        }
        
        // 어르신의 매칭 조회
        Matching matching = matchingRepository.findBySenior(senior)
                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
        
        // 매칭의 스케줄 조회
        Schedule schedule = scheduleRepository.findByMatching(matching)
                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
        
        // 스케줄 상세 정보 조회
        List<ScheduleDetail> scheduleDetails = scheduleDetailRepository.findAllBySchedule(schedule);
        
        // DTO 변환
        List<GetSeniorUpdateFormResponse.ScheduleDto> scheduleDtos = scheduleDetails.stream()
                .map(this::convertToScheduleDto)
                .collect(Collectors.toList());
        
        return new GetSeniorUpdateFormResponse(
                senior.getName(),
                senior.getBirthday(),
                senior.getPhone(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                scheduleDtos,
                senior.getNotes()
        );
    }
    
    private GetSeniorUpdateFormResponse.ScheduleDto convertToScheduleDto(ScheduleDetail detail) {
        return new GetSeniorUpdateFormResponse.ScheduleDto(
                convertDayToString(detail.getDay()),
                detail.getStartTime().toString(),
                detail.getEndTime().toString()
        );
    }
    
    private String convertDayToString(org.example.backend.domain.schedule.model.Day day) {
        return switch (day) {
            case SUN -> "SUN";
            case MON -> "MON";
            case TUE -> "TUE";
            case WED -> "WED";
            case THU -> "THU";
            case FRI -> "FRI";
            case SAT -> "SAT";
        };
    }
} 