package org.example.backend.domain.record.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.model.VolunteerRecordStatus;
import org.example.backend.domain.record.repository.VolunteerRecordRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VolunteerRecordCronService {

    private final VolunteerRecordRepository volunteerRecordRepository;

    /**
     * 30분마다 실행되는 cron job
     * PENDING 상태의 봉사 기록 중 endTime이 현재 시간보다 이전인 것들을 NOT_CONDUCTED로 변경
     */
    @Scheduled(cron = "0 */30 * * * *") // 매 30분마다 실행 (0초, 30분마다, 모든 시간, 모든 일, 모든 월, 모든 요일)
    @Transactional
    public void updateExpiredPendingRecords() {
        log.info("[Cron Job] 만료된 PENDING 봉사 기록 업데이트 시작");
        
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        
        try {
            // 1. 오늘 날짜의 PENDING 레코드 중 endTime이 지난 것들 조회
            List<VolunteerRecord> expiredTodayRecords = volunteerRecordRepository
                    .findPendingRecordsWithExpiredEndTime(today, nowTime);
            
            // 2. 과거 날짜의 PENDING 레코드들 조회
            List<VolunteerRecord> pastDateRecords = volunteerRecordRepository
                    .findPendingRecordsWithPastDate(today);
            
            // 3. 모든 만료된 레코드 합치기
            List<VolunteerRecord> allExpiredRecords = new java.util.ArrayList<>();
            allExpiredRecords.addAll(expiredTodayRecords);
            allExpiredRecords.addAll(pastDateRecords);
            
            if (allExpiredRecords.isEmpty()) {
                log.info("[Cron Job] 만료된 PENDING 봉사 기록이 없습니다.");
                return;
            }
            
            // 4. 레코드 ID 목록 추출
            List<Long> recordIds = allExpiredRecords.stream()
                    .map(VolunteerRecord::getId)
                    .toList();
            
            // 5. 일괄 업데이트 실행
            int updatedCount = volunteerRecordRepository.updatePendingRecordsToNotConducted(recordIds);
            
            log.info("[Cron Job] 만료된 PENDING 봉사 기록 업데이트 완료 - 총 {}개 레코드가 NOT_CONDUCTED로 변경됨", updatedCount);
            
            // 6. 상세 로그 출력 (개발/테스트 환경에서만)
            if (log.isDebugEnabled()) {
                allExpiredRecords.forEach(record -> {
                    log.debug("[Cron Job] 레코드 업데이트 - ID: {}, 매칭ID: {}, 날짜: {}, 시간: {}, 스케줄상세ID: {}", 
                            record.getId(), 
                            record.getMatching().getId(),
                            record.getScheduledDate(),
                            record.getScheduledTime(),
                            record.getScheduleDetail().getId());
                });
            }
            
        } catch (Exception e) {
            log.error("[Cron Job] 만료된 PENDING 봉사 기록 업데이트 중 오류 발생", e);
        }
    }

    /**
     * 매일 자정에 실행되는 cron job (선택사항)
     * 전날의 모든 PENDING 레코드를 NOT_CONDUCTED로 변경
     */
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    @Transactional
    public void updateYesterdayPendingRecords() {
        log.info("[Cron Job] 전날 PENDING 봉사 기록 업데이트 시작");
        
        LocalDate yesterday = LocalDate.now().minusDays(1);
        
        try {
            // 전날의 PENDING 레코드들 조회
            List<VolunteerRecord> yesterdayRecords = volunteerRecordRepository
                    .findPendingRecordsWithPastDate(yesterday);
            
            if (yesterdayRecords.isEmpty()) {
                log.info("[Cron Job] 전날의 PENDING 봉사 기록이 없습니다.");
                return;
            }
            
            // 레코드 ID 목록 추출
            List<Long> recordIds = yesterdayRecords.stream()
                    .map(VolunteerRecord::getId)
                    .toList();
            
            // 일괄 업데이트 실행
            int updatedCount = volunteerRecordRepository.updatePendingRecordsToNotConducted(recordIds);
            
            log.info("[Cron Job] 전날 PENDING 봉사 기록 업데이트 완료 - 총 {}개 레코드가 NOT_CONDUCTED로 변경됨", updatedCount);
            
        } catch (Exception e) {
            log.error("[Cron Job] 전날 PENDING 봉사 기록 업데이트 중 오류 발생", e);
        }
    }

    /**
     * 수동으로 만료된 PENDING 레코드 업데이트 (테스트용)
     */
    @Transactional
    public int manuallyUpdateExpiredPendingRecords() {
        log.info("[Manual] 만료된 PENDING 봉사 기록 수동 업데이트 시작");
        
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        
        // 오늘 날짜의 PENDING 레코드 중 endTime이 지난 것들 조회
        List<VolunteerRecord> expiredTodayRecords = volunteerRecordRepository
                .findPendingRecordsWithExpiredEndTime(today, nowTime);
        
        // 과거 날짜의 PENDING 레코드들 조회
        List<VolunteerRecord> pastDateRecords = volunteerRecordRepository
                .findPendingRecordsWithPastDate(today);
        
        // 모든 만료된 레코드 합치기
        List<VolunteerRecord> allExpiredRecords = new java.util.ArrayList<>();
        allExpiredRecords.addAll(expiredTodayRecords);
        allExpiredRecords.addAll(pastDateRecords);
        
        if (allExpiredRecords.isEmpty()) {
            log.info("[Manual] 만료된 PENDING 봉사 기록이 없습니다.");
            return 0;
        }
        
        // 레코드 ID 목록 추출
        List<Long> recordIds = allExpiredRecords.stream()
                .map(VolunteerRecord::getId)
                .toList();
        
        // 일괄 업데이트 실행
        int updatedCount = volunteerRecordRepository.updatePendingRecordsToNotConducted(recordIds);
        
        log.info("[Manual] 만료된 PENDING 봉사 기록 수동 업데이트 완료 - 총 {}개 레코드가 NOT_CONDUCTED로 변경됨", updatedCount);
        
        return updatedCount;
    }
}
