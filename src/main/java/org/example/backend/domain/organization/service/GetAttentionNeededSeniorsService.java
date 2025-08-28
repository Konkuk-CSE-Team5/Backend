package org.example.backend.domain.organization.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.organization.dto.GetAttentionNeededSeniorsResponse;
import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.organization.repository.OrganizationRepository;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.repository.VolunteerRecordRepository;
import org.example.backend.global.common.exception.CustomException;
import org.example.backend.global.util.DurationUtil;
import org.example.backend.global.util.LocalDateTimeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.NOT_FOUND_USER;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAttentionNeededSeniorsService {
    private final OrganizationRepository organizationRepository;
    private final VolunteerRecordRepository volunteerRecordRepository;
    
    public GetAttentionNeededSeniorsResponse getAttentionNeededSeniors(Long organizationUserId) {
        log.info("[getAttentionNeededSeniors] organizationUserId = {}", organizationUserId);
        
        // 기관 정보 조회
        Organization organization = organizationRepository.findByUserId(organizationUserId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        
        // 이번 주 범위 계산 (오늘이 포함된 월요일 ~ 일요일)
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate weekEnd = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        
        // 기관의 어르신들의 주의가 필요한 기록 조회
        List<VolunteerRecord> attentionNeededRecords = volunteerRecordRepository
                .findAttentionNeededRecordsByOrganizationAndDateRange(organization, weekStart, weekEnd);
        
        // DTO 변환 및 최신순 정렬
        List<GetAttentionNeededSeniorsResponse.AlertDto> alerts = attentionNeededRecords.stream()
                .map(this::convertToAlertDto)
                .collect(Collectors.toList());
        
        return new GetAttentionNeededSeniorsResponse(alerts);
    }
    
    private GetAttentionNeededSeniorsResponse.AlertDto convertToAlertDto(VolunteerRecord record) {
        return new GetAttentionNeededSeniorsResponse.AlertDto(
                record.getId(),
                LocalDateTimeUtil.toFormattedString(record.getScheduledDate()),
                record.getMatching().getSenior().getName(),
                record.getMatching().getVolunteer().getName(),
                record.getVolunteerRecordStatus().name(),
                record.getTotalCallTime() != null ? DurationUtil.toHHmmss(record.getTotalCallTime()) : null
        );
    }
} 