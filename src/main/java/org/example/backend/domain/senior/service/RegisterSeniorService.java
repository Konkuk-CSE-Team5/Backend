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
import org.example.backend.domain.senior.dto.RegisterSeniorRequest;
import org.example.backend.domain.senior.dto.RegisterSeniorResponse;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.senior.repository.SeniorRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.Random;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.NOT_FOUND_USER;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterSeniorService {
    private final SeniorRepository seniorRepository;
    private final OrganizationRepository organizationRepository;
    private final MatchingRepository matchingRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    
    @Transactional
    public RegisterSeniorResponse register(Long organizationUserId, RegisterSeniorRequest request) {
        log.info("[register] organizationUserId = {}, request = {}", organizationUserId, request);

        if (request.startDate() == null || request.endDate() == null) {
            throw new IllegalArgumentException("시작/종료일은 필수입니다.");
        }
        if (request.startDate().isAfter(request.endDate())) {
            throw new IllegalArgumentException("종료일은 시작일 이후여야 합니다.");
        }

        // 기관 정보 조회
        Organization organization = organizationRepository.findByUserId(organizationUserId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        
        // 어르신 코드 생성 (6자리 난수, 중복 방지)
        String seniorCode = generateUniqueSeniorCode();
        
        // 어르신 정보 저장
        Senior senior = Senior.builder()
                .name(request.name().trim())
                .birthday(request.birthday())
                .phone(request.contact().trim())
                .code(seniorCode)
                .organization(organization)
                .build();
        
        Senior savedSenior = seniorRepository.save(senior);
        
        // 초기 매칭은 생성하지 않고, 나중에 봉사자와 매칭될 때 Schedule을 생성
        // 현재는 어르신 등록만 수행
        Matching matching = Matching.builder().matchingStatus(MatchingStatus.PENDING)
                .senior(savedSenior)
                .build();
        Matching savedMatching = matchingRepository.save(matching);

        Schedule schedule = Schedule.builder().startDate(request.startDate())
                .endDate(request.endDate())
                .matching(savedMatching)
                .build();

        scheduleRepository.save(schedule);

        if (request.workDays() != null && !request.workDays().isEmpty()) {

            if (request.workStartTime() == null || request.workEndTime() == null) {
                throw new IllegalArgumentException("근무 시작/종료 시간은 필수입니다.");
            }
            if (!request.workStartTime().isBefore(request.workEndTime())) {
                throw new IllegalArgumentException("근무 종료 시간은 시작 시간보다 이후여야 합니다.");
            }

            for (String dayStr : request.workDays()) {
                ScheduleDetail detail = ScheduleDetail.builder().day(convertStringToDay(dayStr))
                        .startTime(request.workStartTime())
                        .endTime(request.workEndTime())
                        .schedule(schedule)
                        .build();
                scheduleDetailRepository.save(detail);
            }
        }

        log.info("[register] 어르신 등록 완료 - seniorId: {}, code: {}", savedSenior.getId(), seniorCode);
        
        return new RegisterSeniorResponse(seniorCode, organization.getName());
    }
    
    private String generateUniqueSeniorCode() {
        Random random = new Random();
        String code;
        do {
            // 6자리 난수 생성 (100000 ~ 999999)
            code = String.valueOf(100000 + random.nextInt(900000));
        } while (seniorRepository.existsByCode(code));
        
        return code;
    }
    
    private Day convertStringToDay(String dayStr) {
        return switch (dayStr) {
            case "일" -> Day.SUN;
            case "월" -> Day.MON;
            case "화" -> Day.TUE;
            case "수" -> Day.WED;
            case "목" -> Day.THU;
            case "금" -> Day.FRI;
            case "토" -> Day.SAT;
            default -> throw new IllegalArgumentException("잘못된 요일: " + dayStr);
        };
    }
}