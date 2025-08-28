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
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.model.VolunteerRecordStatus;
import org.example.backend.domain.record.repository.VolunteerRecordRepository;

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
import org.springframework.transaction.annotation.Propagation;
import org.hibernate.exception.ConstraintViolationException;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

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
    private final VolunteerRecordRepository volunteerRecordRepository;


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

        // 기존 매칭에 봉사자 정보 업데이트 및 상태를 ACTIVE로 변경
        existingMatching.updateVolunteer(volunteer);
        existingMatching.updateMatchingStatus(MatchingStatus.ACTIVE);
        Matching updatedMatching = matchingRepository.save(existingMatching);
        log.info("기존 매칭에 봉사자 정보 업데이트 및 상태를 ACTIVE로 변경 - matchingId: {}, volunteerId: {}",
                updatedMatching.getId(), volunteer.getId());

        // 스케줄에 맞는 VolunteerRecord들을 미리 생성 (트랜잭션 분리로 세션 오염 방지)
        // 매칭의 스케줄 조회 (1:1 관계)
        Schedule schedule = scheduleRepository.findByMatching(updatedMatching).orElse(null);
        if (schedule != null) {
            // 스케줄 상세 정보 조회 (1:다 관계)
            List<ScheduleDetail> scheduleDetails = scheduleDetailRepository.findAllBySchedule(schedule);

            if (!scheduleDetails.isEmpty()) {
                // 스케줄 기간 내의 모든 날짜에 대해 VolunteerRecord 생성
                LocalDate currentDate = schedule.getStartDate();
                LocalDate endDate = schedule.getEndDate();

                int createdCount = 0;
                while (!currentDate.isAfter(endDate)) {
                    // 현재 날짜의 요일 확인
                    java.time.DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();

                    // 해당 요일의 스케줄 상세 정보 찾기
                    for (ScheduleDetail detail : scheduleDetails) {
                        if (convertDayToDayOfWeek(detail.getDay()) == currentDayOfWeek) {
                            try {
                                createdCount++;
                                VolunteerRecord record = VolunteerRecord.builder()
                                        .volunteerRecordStatus(VolunteerRecordStatus.PENDING)
                                        .scheduledDate(currentDate)
                                        .scheduledTime(detail.getStartTime())
                                        .scheduleDetail(detail)
                                        .matching(updatedMatching)
                                        .build();
                                volunteerRecordRepository.save(record);
                                log.info("VolunteerRecord 생성 - date: {}, time: {}, day: {}",
                                        currentDate, detail.getStartTime(), detail.getDay());

//                                boolean created = createRecordIfAbsent(detail, currentDate, updatedMatching);
//                                if (created) {
//                                    createdCount++;
//                                    log.info("VolunteerRecord 생성 - date: {}, time: {}, day: {}",
//                                            currentDate, detail.getStartTime(), detail.getDay());
//                                } else {
//                                    log.info("VolunteerRecord 이미 존재 - date: {}, time: {}, day: {}",
//                                            currentDate, detail.getStartTime(), detail.getDay());
//                                }
                            } catch (Exception ex) {
                                // 다른 에러인 경우 로그만 남기고 계속 진행 (트랜잭션은 분리되어 영향 없음)
                                log.warn("VolunteerRecord 생성 실패 - date: {}, time: {}, day: {}, error: {}",
                                        currentDate, detail.getStartTime(), detail.getDay(), ex.getMessage());
                            }
                        }
                    }

                    currentDate = currentDate.plusDays(1);
                }

                log.info("VolunteerRecord 생성 완료 - matchingId: {}, scheduleId: {}, createdCount: {}",
                        updatedMatching.getId(), schedule.getId(), createdCount);
            } else {
                log.warn("스케줄 상세 정보가 없음 - scheduleId: {}", schedule.getId());
            }
        } else {
            log.warn("매칭에 대한 스케줄을 찾을 수 없음 - matchingId: {}", updatedMatching.getId());
        }

        // 어르신 정보와 스케줄 조회하여 응답 생성
        return createResponse(updatedMatching);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean createRecordIfAbsent(ScheduleDetail detail, LocalDate date, Matching matching) {
        // 이미 존재하면 생성하지 않음
        boolean exists = volunteerRecordRepository.existsByScheduleDetailAndScheduledDate(detail, date);
        if (exists) return false;

        try {
            VolunteerRecord record = VolunteerRecord.builder()
                    .volunteerRecordStatus(VolunteerRecordStatus.PENDING)
                    .scheduledDate(date)
                    .scheduledTime(detail.getStartTime())
                    .scheduleDetail(detail)
                    .matching(matching)
                    .build();
            volunteerRecordRepository.save(record);
            return true;
        } catch (org.springframework.dao.DataIntegrityViolationException | ConstraintViolationException dup) {
            // 동시성으로 인해 유니크 제약 위반 시 생성하지 않은 것으로 처리
            log.debug("동시성으로 인한 중복 발생으로 생성 건너뜀 - detailId: {}, date: {}", detail.getId(), date);
            return false;
        }
    }

    private RegisterCodeResponse createResponse(Matching matching) {
        // 매칭에서 어르신 정보 조회
        Senior senior = matching.getSenior();
        if (senior == null) {
            log.warn("매칭에서 어르신 정보를 찾을 수 없음 - matchingId: {}", matching.getId());
            throw new CustomException(ENTITY_NOT_FOUND);
        }

        // 스케줄 정보 조회 (1:1 관계)
        Schedule schedule = scheduleRepository.findByMatching(matching).orElse(null);
        List<RegisterCodeResponse.ScheduleDto> scheduleDtos = List.of();
        LocalDate nextSchedule = null;

        if (schedule != null) {
            // 스케줄 상세 정보 조회 (1:다 관계)
            List<ScheduleDetail> scheduleDetails = scheduleDetailRepository.findAllBySchedule(schedule);
            
            scheduleDtos = scheduleDetails.stream()
                    .map(detail -> RegisterCodeResponse.ScheduleDto.builder()
                            .day(convertDayToString(detail.getDay()))
                            .time(detail.getStartTime())
                            .build())
                    .toList();

            // 다음 일정 계산
            nextSchedule = calculateNextSchedule(scheduleDetails);
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
