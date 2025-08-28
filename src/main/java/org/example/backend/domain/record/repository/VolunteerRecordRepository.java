package org.example.backend.domain.record.repository;

import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.model.VolunteerRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface VolunteerRecordRepository extends JpaRepository<VolunteerRecord, Long> {
    List<VolunteerRecord> findAllByMatchingOrderByIdDesc(Matching matching);

    // 주간: scheduledDate 기준
    List<VolunteerRecord> findAllByMatchingAndScheduledDateBetweenOrderByIdDesc(
            Matching matching, LocalDate startInclusive, LocalDate endInclusive);

    // 다음 예정(PENDING) 1건: 오늘 이후 또는 (오늘 & 현재시간 이후) 중 가장 이른 것
    @Query("""
        select vr
        from VolunteerRecord vr
        where vr.matching = :matching
          and vr.volunteerRecordStatus = :status
          and (
               vr.scheduledDate > :today
            or (vr.scheduledDate = :today and vr.scheduledTime >= :nowTime)
          )
        order by vr.scheduledDate asc, vr.scheduledTime asc
        """)
    Optional<VolunteerRecord> findNextPending(
            @Param("matching") Matching matching,
            @Param("status") VolunteerRecordStatus status,
            @Param("today") LocalDate today,
            @Param("nowTime") LocalTime nowTime
    );

    // 이번 달 전체(상태 무관)
    int countByMatchingAndScheduledDateGreaterThanEqualAndScheduledDateLessThan(
            Matching matching, LocalDate monthStart, LocalDate monthEndExclusive);

    // 이번 달 완료(COMPLETE)
    int countByMatchingAndVolunteerRecordStatusAndScheduledDateGreaterThanEqualAndScheduledDateLessThan(
            Matching matching, VolunteerRecordStatus status,
            LocalDate monthStart, LocalDate monthEndExclusive);


    // 특정 매칭 + 오늘 날짜에 해당하는 VolunteerRecord 조회
    Optional<VolunteerRecord> findByMatchingAndScheduledDate(Matching matching, LocalDate scheduledDate);

    Optional<VolunteerRecord> findTopByMatchingOrderByIdDesc(Matching matching);

    // 기관의 어르신들의 주의가 필요한 기록 조회 (이번 주, ABSENT 또는 NOT_CONDUCTED)
    @Query("""
        select vr
        from VolunteerRecord vr
        join vr.matching m
        join m.senior s
        where s.organization = :organization
          and vr.scheduledDate between :startDate and :endDate
          and vr.volunteerRecordStatus in ('ABSENT', 'NOT_CONDUCTED')
        order by vr.id desc
        """)
    List<VolunteerRecord> findAttentionNeededRecordsByOrganizationAndDateRange(
            @Param("organization") Organization organization,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
