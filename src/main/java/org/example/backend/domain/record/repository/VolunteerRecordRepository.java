package org.example.backend.domain.record.repository;

import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.model.VolunteerRecordStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    List<VolunteerRecord> findNextPending(
            @Param("matching") Matching matching,
            @Param("status") VolunteerRecordStatus status,
            @Param("today") LocalDate today,
            @Param("nowTime") LocalTime nowTime,
            Pageable pageable);

    // 이번 달 전체(상태 무관)
    int countByMatchingAndScheduledDateGreaterThanEqualAndScheduledDateLessThan(
            Matching matching, LocalDate monthStart, LocalDate monthEndExclusive);

    // 이번 달 완료(COMPLETE)
    int countByMatchingAndVolunteerRecordStatusAndScheduledDateGreaterThanEqualAndScheduledDateLessThan(
            Matching matching, VolunteerRecordStatus status,
            LocalDate monthStart, LocalDate monthEndExclusive);


    // 특정 매칭 + 오늘 날짜에 해당하는 VolunteerRecord 조회
    Optional<VolunteerRecord> findByMatchingAndScheduledDate(Matching matching, LocalDate scheduledDate);
    
    // 특정 스케줄 상세 정보 + 날짜에 해당하는 VolunteerRecord 존재 여부 확인
    boolean existsByScheduleDetailAndScheduledDate(org.example.backend.domain.schedule.model.ScheduleDetail scheduleDetail, LocalDate scheduledDate);

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

    // =====================================================
    // Cron Job 관련 메서드들
    // =====================================================

    /**
     * 오늘 날짜의 PENDING 레코드 중 endTime이 지난 것들 조회
     * 
     * JPQL에서 LocalTime 연산이 제한적이므로,
     * scheduledTime이 현재 시간보다 이전인 경우로 단순화
     * 실제 endTime 계산은 서비스 레이어에서 처리
     */
    @Query("""
        select vr
        from VolunteerRecord vr
        where vr.volunteerRecordStatus = 'PENDING'
          and vr.scheduledDate = :today
          and vr.scheduledTime < :nowTime
        """)
    List<VolunteerRecord> findPendingRecordsWithExpiredEndTime(
            @Param("today") LocalDate today,
            @Param("nowTime") LocalTime nowTime
    );

    /**
     * 과거 날짜의 PENDING 레코드들 조회
     */
    @Query("""
        select vr
        from VolunteerRecord vr
        where vr.volunteerRecordStatus = 'PENDING'
          and vr.scheduledDate < :today
        """)
    List<VolunteerRecord> findPendingRecordsWithPastDate(@Param("today") LocalDate today);

    /**
     * PENDING 레코드들을 NOT_CONDUCTED로 일괄 업데이트
     */
    @Modifying
    @Query("""
        update VolunteerRecord vr
        set vr.volunteerRecordStatus = 'NOT_CONDUCTED',
            vr.updatedAt = current_timestamp
        where vr.id in :recordIds
          and vr.volunteerRecordStatus = 'PENDING'
        """)
    int updatePendingRecordsToNotConducted(@Param("recordIds") List<Long> recordIds);

}
