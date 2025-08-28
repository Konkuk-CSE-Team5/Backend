package org.example.backend.domain.volunteer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.matching.repository.MatchingRepository;
import org.example.backend.domain.schedule.model.Day;
import org.example.backend.domain.schedule.model.Schedule;
import org.example.backend.domain.schedule.model.ScheduleDetail;
import org.example.backend.domain.schedule.repository.ScheduleDetailRepository;
import org.example.backend.domain.schedule.repository.ScheduleRepository;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.senior.repository.SeniorRepository;
import org.example.backend.domain.users.model.User;
import org.example.backend.domain.users.repository.UserRepository;
import org.example.backend.domain.volunteer.dto.RegisterCodeResponse;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.domain.volunteer.repository.VolunteerRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;
import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.ENTITY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class VolunteerCodeService {
    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    private final SeniorRepository seniorRepository;
    private final MatchingRepository matchingRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;

    @Transactional
    public RegisterCodeResponse registerCode(Long loginUserId, String code) {
        log.info("코드 등록 시도 - userId: {}, code: {}", loginUserId, code);

        // 봉사자 정보 조회
        User user = userRepository.findById(loginUserId).orElseThrow(() -> new CustomException(BAD_REQUEST));
        Volunteer volunteer = volunteerRepository.findByUser(user).orElseThrow(() -> new CustomException(BAD_REQUEST));

        // 코드로 어르신 조회
        Senior senior = seniorRepository.findByCode(code).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
        log.info("어르신 발견 - seniorId: {}, name: {}", senior.getId(), senior.getName());

        // 어르신의 기존 매칭 조회 (PENDING 상태의 매칭)
        Matching existingMatching = matchingRepository.findBySeniorIdAndMatchingStatus(senior.getId(), MatchingStatus.PENDING)
                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
        
        // 이미 다른 봉사자가 매칭되어 있는지 확인
        if (existingMatching.getVolunteer() != null) {
            log.warn("이미 다른 봉사자가 매칭된 어르신입니다 - seniorId: {}, volunteerId: {}", 
                    senior.getId(), existingMatching.getVolunteer().getId());
            throw new CustomException(BAD_REQUEST);
        }

        // 기존 매칭에 봉사자 정보 업데이트
        existingMatching.updateVolunteer(volunteer);
        Matching updatedMatching = matchingRepository.save(existingMatching);
        log.info("기존 매칭에 봉사자 정보 업데이트 - matchingId: {}, volunteerId: {}", 
                updatedMatching.getId(), volunteer.getId());

        // 어르신 정보와 스케줄 조회하여 응답 생성
        return createResponse(senior);
    }

    private RegisterCodeResponse createResponse(Senior senior) {
        // 어르신의 매칭 조회 (PENDING 상태의 매칭)
        Matching matching = matchingRepository.findBySeniorAndMatchingStatus(senior, MatchingStatus.PENDING)
                .orElse(null);
        
        if (matching == null) {
            log.warn("어르신의 PENDING 매칭을 찾을 수 없음 - seniorId: {}", senior.getId());
            throw new CustomException(ENTITY_NOT_FOUND);
        }

        // 스케줄 정보 조회
        List<Schedule> schedules = scheduleRepository.findAllByMatching(matching);
        List<ScheduleDetail> allScheduleDetails = scheduleDetailRepository.findAllByScheduleIn(schedules);
        Map<Long, List<ScheduleDetail>> scheduleToDetails = allScheduleDetails.stream()
                .collect(Collectors.groupingBy(sd -> sd.getSchedule().getId()));

        // 스케줄 정보 구성
        List<RegisterCodeResponse.ScheduleDto> scheduleDtos = List.of();
        LocalDate nextSchedule = null;

        if (!schedules.isEmpty()) {
            Schedule schedule = schedules.get(0); // 첫 번째 스케줄 사용
            List<ScheduleDetail> details = scheduleToDetails.getOrDefault(schedule.getId(), List.of());
            
            scheduleDtos = details.stream()
                    .map(detail -> RegisterCodeResponse.ScheduleDto.builder()
                            .day(convertDayToString(detail.getDay()))
                            .time(detail.getStartTime())
                            .build())
                    .toList();

            // 다음 일정 계산
            nextSchedule = calculateNextSchedule(details);
        }

        return RegisterCodeResponse.builder()
                .name(senior.getName())
                .schedule(scheduleDtos)
                .notes(senior.getNotes())
                .nextSchedule(nextSchedule)
                .build();
    }

    private String convertDayToString(Day day) {
        return switch (day) {
            case SUN -> "Sunday";
            case MON -> "Monday";
            case TUE -> "Tuesday";
            case WED -> "Wednesday";
            case THU -> "Thursday";
            case FRI -> "Friday";
            case SAT -> "Saturday";
        };
    }

    private LocalDate calculateNextSchedule(List<ScheduleDetail> scheduleDetails) {
        if (scheduleDetails.isEmpty()) {
            return null;
        }

        LocalDate today = LocalDate.now();
        LocalDate nextDate = null;

        // 각 요일에 대해 다음 발생 날짜 계산
        for (ScheduleDetail detail : scheduleDetails) {
            DayOfWeek targetDay = convertDayToDayOfWeek(detail.getDay());
            LocalDate candidateDate = today;

            // 다음 해당 요일 찾기
            while (candidateDate.getDayOfWeek() != targetDay || candidateDate.equals(today)) {
                candidateDate = candidateDate.plusDays(1);
            }

            // 가장 가까운 날짜 선택
            if (nextDate == null || candidateDate.isBefore(nextDate)) {
                nextDate = candidateDate;
            }
        }

        return nextDate;
    }

    private DayOfWeek convertDayToDayOfWeek(Day day) {
        return switch (day) {
            case SUN -> DayOfWeek.SUNDAY;
            case MON -> DayOfWeek.MONDAY;
            case TUE -> DayOfWeek.TUESDAY;
            case WED -> DayOfWeek.WEDNESDAY;
            case THU -> DayOfWeek.THURSDAY;
            case FRI -> DayOfWeek.FRIDAY;
            case SAT -> DayOfWeek.SATURDAY;
        };
    }
}
