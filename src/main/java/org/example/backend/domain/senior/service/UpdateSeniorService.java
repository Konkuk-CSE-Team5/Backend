package org.example.backend.domain.senior.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.matching.repository.MatchingRepository;
import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.organization.repository.OrganizationRepository;
import org.example.backend.domain.schedule.model.Day;
import org.example.backend.domain.schedule.model.Schedule;
import org.example.backend.domain.schedule.model.ScheduleDetail;
import org.example.backend.domain.schedule.repository.ScheduleDetailRepository;
import org.example.backend.domain.schedule.repository.ScheduleRepository;
import org.example.backend.domain.senior.dto.PatchSeniorRequest;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.senior.repository.SeniorRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;
import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.ENTITY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class UpdateSeniorService {
    private final SeniorRepository seniorRepository;
    private final OrganizationRepository organizationRepository;
    private final MatchingRepository matchingRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    
    @Transactional
    public void updateSenior(Long organizationUserId, Long seniorId, PatchSeniorRequest request) {
        log.info("[updateSenior] organizationUserId = {}, seniorId = {}, request = {}", 
                organizationUserId, seniorId, request);

        // 기관 정보 조회
        Organization organization = organizationRepository.findByUserId(organizationUserId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));
        
        // 어르신 정보 조회
        Senior senior = seniorRepository.findById(seniorId)
                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
        
        // 권한 체크: 어르신이 해당 기관에 속하는지 확인
        if (!senior.getOrganization().equals(organization)) {
            log.warn("[updateSenior] 권한 없음 - organizationUserId: {}, seniorId: {}", 
                    organizationUserId, seniorId);
            throw new CustomException(BAD_REQUEST);
        }
        
        // 날짜 유효성 검사 (둘 다 null이 아닌 경우에만)
        if (request.startDate() != null && request.endDate() != null && request.startDate().isAfter(request.endDate())) {
            throw new CustomException(BAD_REQUEST);
        }
        
        // 어르신 기본 정보 업데이트 (null이 아닌 값들만)
        String newName = request.name() != null ? request.name().trim() : senior.getName();
        LocalDate newBirthday = request.birthday() != null ? request.birthday() : senior.getBirthday();
        String newContact = request.contact() != null ? request.contact().trim() : senior.getPhone();
        String newNotes = request.notes() != null ? request.notes().trim() : senior.getNotes();
        
        senior.updateSenior(newName, newBirthday, newContact, newNotes);
        seniorRepository.save(senior);
        
        // 어르신의 ACTIVE 매칭 조회 (봉사자와 매칭된 상태)
        Matching matching = matchingRepository.findBySeniorIdAndMatchingStatus(seniorId, MatchingStatus.ACTIVE)
                .orElse(null);
        
        if (matching != null) {
            // 매칭의 스케줄 조회 및 업데이트 (null이 아닌 값들만)
            Schedule schedule = scheduleRepository.findByMatching(matching).orElse(null);
            if (schedule != null) {
                LocalDate newStartDate = request.startDate() != null ? request.startDate() : schedule.getStartDate();
                LocalDate newEndDate = request.endDate() != null ? request.endDate() : schedule.getEndDate();
                
                schedule.updateSchedule(newStartDate, newEndDate);
                scheduleRepository.save(schedule);
                log.info("[updateSenior] 스케줄 업데이트 완료 - scheduleId: {}", schedule.getId());
                
                // 스케줄 상세 정보 업데이트 (스케줄이 제공된 경우)
                if (request.schedule() != null && !request.schedule().isEmpty()) {
                    updateScheduleDetails(schedule, request.schedule());
                }
            }
        }
        
        log.info("[updateSenior] 어르신 정보 수정 완료 - seniorId: {}", seniorId);
    }
    
    private void updateScheduleDetails(Schedule schedule, List<PatchSeniorRequest.ScheduleDto> scheduleDtos) {
        // 기존 스케줄 상세 정보 삭제
        List<ScheduleDetail> existingDetails = scheduleDetailRepository.findAllBySchedule(schedule);
        scheduleDetailRepository.deleteAll(existingDetails);
        
        // 새로운 스케줄 상세 정보 생성
        for (PatchSeniorRequest.ScheduleDto scheduleDto : scheduleDtos) {
            // 시간 유효성 검사
            if (scheduleDto.startTime().isAfter(scheduleDto.endTime())) {
                throw new CustomException(BAD_REQUEST);
            }
            
            ScheduleDetail detail = ScheduleDetail.builder()
                    .day(convertStringToDay(scheduleDto.day()))
                    .startTime(scheduleDto.startTime())
                    .endTime(scheduleDto.endTime())
                    .schedule(schedule)
                    .build();
            
            scheduleDetailRepository.save(detail);
        }
        
        log.info("[updateScheduleDetails] 스케줄 상세 정보 업데이트 완료 - scheduleId: {}, detailCount: {}", 
                schedule.getId(), scheduleDtos.size());
    }
    
    private Day convertStringToDay(String dayStr) {
        return switch (dayStr.toLowerCase()) {
            case "sunday", "sun" -> Day.SUN;
            case "monday", "mon" -> Day.MON;
            case "tuesday", "tue" -> Day.TUE;
            case "wednesday", "wed" -> Day.WED;
            case "thursday", "thu" -> Day.THU;
            case "friday", "fri" -> Day.FRI;
            case "saturday", "sat" -> Day.SAT;
            default -> throw new CustomException(BAD_REQUEST);
        };
    }
}
