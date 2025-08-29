package org.example.backend.domain.volunteer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.repository.MatchingRepository;
import org.example.backend.domain.schedule.model.Day;
import org.example.backend.domain.schedule.model.Schedule;
import org.example.backend.domain.schedule.model.ScheduleDetail;
import org.example.backend.domain.schedule.repository.ScheduleDetailRepository;
import org.example.backend.domain.schedule.repository.ScheduleRepository;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.users.model.User;
import org.example.backend.domain.users.repository.UserRepository;
import org.example.backend.domain.volunteer.dto.GetVolunteerMainResponse;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.domain.volunteer.repository.VolunteerRepository;
import org.example.backend.global.common.exception.CustomException;
import org.example.backend.global.util.AgeUtils;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;

@Slf4j
@RequiredArgsConstructor
@Service
public class VolunteerMainService {
    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    private final MatchingRepository matchingRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;

    public GetVolunteerMainResponse getMain(Long loginUserId) {
        // 봉사자 정보 조회
        User user = userRepository.findById(loginUserId).orElseThrow(() -> new CustomException(BAD_REQUEST));
        Volunteer volunteer = volunteerRepository.findByUser(user).orElseThrow(() -> new CustomException(BAD_REQUEST));

        // 봉사자의 모든 매칭 조회
        List<Matching> matchings = matchingRepository.findAllByVolunteer(volunteer);
        
        if (matchings.isEmpty()) {
            return GetVolunteerMainResponse.builder()
                    .seniors(List.of())
                    .build();
        }

        // 각 매칭에 대한 스케줄 조회
        List<Schedule> schedules = scheduleRepository.findAllByMatchingIn(matchings);
        Map<Long, Schedule> matchingToSchedule = schedules.stream()
                .collect(Collectors.toMap(s -> s.getMatching().getId(), s -> s));

        // 모든 스케줄의 상세 정보 조회
        List<ScheduleDetail> allScheduleDetails = scheduleDetailRepository.findAllByScheduleIn(schedules);
        Map<Long, List<ScheduleDetail>> scheduleToDetails = allScheduleDetails.stream()
                .collect(Collectors.groupingBy(sd -> sd.getSchedule().getId()));

        // 어르신별 정보 구성
        List<GetVolunteerMainResponse.SeniorDto> seniorDtos = matchings.stream()
                .map(matching -> {
                    Senior senior = matching.getSenior();
                    Schedule schedule = matchingToSchedule.get(matching.getId());
                    
                    // 스케줄 정보 구성
                    List<GetVolunteerMainResponse.ScheduleDto> scheduleDtos = List.of();
                    LocalDate nextSchedule = null;
                    
                    if (schedule != null) {
                        List<ScheduleDetail> details = scheduleToDetails.getOrDefault(schedule.getId(), List.of());
                        scheduleDtos = details.stream()
                                .map(detail -> GetVolunteerMainResponse.ScheduleDto.builder()
                                        .day(convertDayToString(detail.getDay()))
                                        .startTime(detail.getStartTime())
                                        .endTime(detail.getEndTime())
                                        .build())
                                .toList();
                        
                        // 다음 일정 계산 (가장 가까운 미래 날짜)
                        nextSchedule = calculateNextSchedule(details);
                    }

                    return GetVolunteerMainResponse.SeniorDto.builder()
                            .seniorId(senior.getId())
                            .name(senior.getName())
                            .age(AgeUtils.toAge(senior.getBirthday()))
                            .phone(senior.getPhone())
                            .schedule(scheduleDtos)
                            .notes(senior.getNotes())
                            .nextSchedule(nextSchedule)
                            .build();
                })
                .toList();

        return GetVolunteerMainResponse.builder()
                .seniors(seniorDtos)
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
