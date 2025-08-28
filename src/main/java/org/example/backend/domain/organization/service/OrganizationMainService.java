package org.example.backend.domain.organization.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.matching.repository.MatchingRepository;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.organization.dto.GetOrganziationMainResponse;
import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.organization.repository.OrganizationRepository;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.model.VolunteerRecordStatus;
import org.example.backend.domain.record.repository.VolunteerRecordRepository;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.senior.repository.SeniorRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.ENTITY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrganizationMainService {
    private final OrganizationRepository organizationRepository;
    private final SeniorRepository seniorRepository;
    private final MatchingRepository matchingRepository;
    private final VolunteerRecordRepository volunteerRecordRepository;
    public GetOrganziationMainResponse getMain(Long userId) {
        // 1) 기관, 어르신, 활성 매칭 수집
        Organization organization = organizationRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));

        List<Senior> seniors = seniorRepository.findAllByOrganization(organization);

        List<Matching> allCurrentMatching = new ArrayList<>();
        for (Senior senior : seniors) {
            allCurrentMatching.addAll(
                    matchingRepository.findAllBySeniorAndMatchingStatus(senior, MatchingStatus.ACTIVE)
            );
        }

        // 2) "이번 주" 범위 계산: 오늘이 포함된 월요일 ~ 일요일
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate weekEnd   = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        // scheduledDate BETWEEN weekStart AND weekEnd (둘 다 포함)

        // 3) 주간 레코드 수집 (scheduledDate 기준)
        List<VolunteerRecord> weeklyRecords = new ArrayList<>();
        for (Matching matching : allCurrentMatching) {
            weeklyRecords.addAll(
                    volunteerRecordRepository.findAllByMatchingAndScheduledDateBetweenOrderByIdDesc(
                            matching, weekStart, weekEnd
                    )
            );
        }

        // 4) 주간 통계 집계
        Map<VolunteerRecordStatus, Long> countByStatus = weeklyRecords.stream()
                .collect(Collectors.groupingBy(
                        VolunteerRecord::getVolunteerRecordStatus,
                        Collectors.counting()
                ));

        long totalCount     = weeklyRecords.size();
        long completedCount = countByStatus.getOrDefault(VolunteerRecordStatus.COMPLETE, 0L);
        long absentCount    = countByStatus.getOrDefault(VolunteerRecordStatus.ABSENT, 0L);
        long missedCount    = countByStatus.getOrDefault(VolunteerRecordStatus.NOT_CONDUCTED, 0L);
        long pendingCount   = countByStatus.getOrDefault(VolunteerRecordStatus.PENDING, 0L);

        int progressRate = totalCount == 0 ? 0 : (int) Math.round((completedCount * 100.0) / totalCount);

        GetOrganziationMainResponse.WeeklyVolunteerStatus weekly =
                new GetOrganziationMainResponse.WeeklyVolunteerStatus(
                        progressRate,
                        (int) totalCount,
                        (int) completedCount,
                        (int) absentCount,
                        (int) missedCount,
                        (int) pendingCount
                );

        // 5) 주의 필요 목록 (ABSENT | NOT_CONDUCTED) — 이번 주 안에서만
        List<GetOrganziationMainResponse.VolunteerNeedingAttention> volunteersNeedingAttention =
                weeklyRecords.stream()
                        .filter(v -> v.getVolunteerRecordStatus() == VolunteerRecordStatus.ABSENT
                                || v.getVolunteerRecordStatus() == VolunteerRecordStatus.NOT_CONDUCTED)
                        .sorted(Comparator
                                .comparing(VolunteerRecord::getScheduledDate, Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(VolunteerRecord::getScheduledTime, Comparator.nullsLast(Comparator.naturalOrder()))
                                .reversed())
                        .map(v -> new GetOrganziationMainResponse.VolunteerNeedingAttention(
                                v.getScheduledDate(),
                                v.getMatching().getSenior().getName(),
                                v.getVolunteerRecordStatus().name()
                        ))
                        .toList();

        // 6) 어르신별 현황 카드
        YearMonth thisMonth = YearMonth.now();
        LocalDate monthStart = thisMonth.atDay(1);
        LocalDate monthEndExclusive = thisMonth.plusMonths(1).atDay(1);
        LocalTime nowTime = LocalTime.now();

        List<GetOrganziationMainResponse.SeniorStatus> seniorStatuses =
                allCurrentMatching.stream()
                        .map(m -> {
                            // 다음 예정: PENDING 중 오늘/현재 이후 최가까운 1건
                            LocalDateTime nextSchedule = volunteerRecordRepository
                                    .findNextPending(m, VolunteerRecordStatus.PENDING, today, nowTime, PageRequest.of(0, 1))
                                    .stream().findFirst()
                                    .map(vr -> LocalDateTime.of(vr.getScheduledDate(), vr.getScheduledTime()))
                                    .orElse(null);

                            // 이번 달 게이지: COMPLETE / 전체 (scheduledDate 기준)
                            int target = volunteerRecordRepository
                                    .countByMatchingAndScheduledDateGreaterThanEqualAndScheduledDateLessThan(
                                            m, monthStart, monthEndExclusive);

                            int completed = volunteerRecordRepository
                                    .countByMatchingAndVolunteerRecordStatusAndScheduledDateGreaterThanEqualAndScheduledDateLessThan(
                                            m, VolunteerRecordStatus.COMPLETE, monthStart, monthEndExclusive);

                            return new GetOrganziationMainResponse.SeniorStatus(
                                    m.getSenior().getId(),
                                    m.getSenior().getName(),
                                    calculateAge(m.getSenior().getBirthday()),   // ✅ 나이 계산
                                    m.getVolunteer().getName(),
                                    nextSchedule,
                                    new GetOrganziationMainResponse.SeniorStatus.MonthlyCalls(completed, target)
                            );
                        })
                        .sorted(Comparator.comparing(
                                (GetOrganziationMainResponse.SeniorStatus s) ->
                                        s.nextSchedule() == null ? LocalDateTime.MAX : s.nextSchedule()
                        ))
                        .toList();

        // 7) 최종 응답
        return new GetOrganziationMainResponse(
                weekly,
                volunteersNeedingAttention,
                seniorStatuses
        );
    }

    private int calculateAge(LocalDate birthday) {
        if (birthday == null) {
            return 0; // 또는 예외 처리/Optional로 대체
        }
        return Period.between(birthday, LocalDate.now()).getYears();
    }
}
