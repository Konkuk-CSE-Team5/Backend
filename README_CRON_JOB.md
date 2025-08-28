# VolunteerRecord Cron Job 기능

## 📋 개요
30분마다 자동으로 실행되는 cron job을 통해 `PENDING` 상태의 봉사 기록 중 `endTime`이 현재 시간보다 이전인 것들을 `NOT_CONDUCTED`로 변경하는 기능입니다.

## 🔧 구현된 기능

### 1. 자동 Cron Job
- **실행 주기**: 30분마다 (`0 */30 * * * *`)
- **기능**: 
  - 오늘 날짜의 PENDING 레코드 중 `endTime`이 현재 시간보다 이전인 것들 조회
  - 과거 날짜의 모든 PENDING 레코드 조회
  - 해당 레코드들을 `NOT_CONDUCTED`로 일괄 업데이트

### 2. 추가 Cron Job (선택사항)
- **실행 주기**: 매일 자정 (`0 0 0 * * *`)
- **기능**: 전날의 모든 PENDING 레코드를 `NOT_CONDUCTED`로 변경

### 3. 수동 실행 API
- **엔드포인트**: `POST /api/volunteer-records/cron/update-expired`
- **기능**: 테스트용으로 수동으로 만료된 PENDING 레코드 업데이트

## 📁 관련 파일

### 1. Repository
- `VolunteerRecordRepository.java`
  - `findPendingRecordsWithExpiredEndTime()`: 오늘 날짜의 만료된 PENDING 레코드 조회
  - `findPendingRecordsWithPastDate()`: 과거 날짜의 PENDING 레코드 조회
  - `updatePendingRecordsToNotConducted()`: PENDING 레코드를 NOT_CONDUCTED로 일괄 업데이트

### 2. Service
- `VolunteerRecordCronService.java`
  - `updateExpiredPendingRecords()`: 30분마다 실행되는 메인 cron job
  - `updateYesterdayPendingRecords()`: 매일 자정 실행되는 cron job
  - `manuallyUpdateExpiredPendingRecords()`: 수동 실행 메서드

### 3. Controller
- `VolunteerRecordCronController.java`
  - 수동 실행을 위한 REST API 제공

### 4. Configuration
- `BackendApplication.java`: `@EnableScheduling` 어노테이션 추가
- `application.yml`: cron job 스레드 풀 설정

## 🚀 사용 방법

### 1. 자동 실행
애플리케이션을 시작하면 자동으로 cron job이 활성화됩니다.

### 2. 수동 실행 (테스트용)
```bash
# API 호출
curl -X POST http://localhost:8080/api/volunteer-records/cron/update-expired
```

### 3. 로그 확인
```bash
# 애플리케이션 로그에서 cron job 실행 확인
grep "Cron Job" application.log
```

## ⚙️ 설정

### 1. Cron 표현식
- **30분마다**: `0 */30 * * * *`
- **매일 자정**: `0 0 0 * * *`
- **매시간**: `0 0 * * * *`
- **매일 오후 2시**: `0 0 14 * * *`

### 2. 스레드 풀 설정
```yaml
spring:
  task:
    scheduling:
      pool:
        size: 5  # 동시 실행 가능한 cron job 수
      thread-name-prefix: "cron-"  # 스레드 이름 접두사
```

## 📊 동작 방식

### 1. 만료 조건 확인
```sql
-- 오늘 날짜의 PENDING 레코드 중 endTime이 현재 시간보다 이전인 것들
SELECT vr.*
FROM volunteer_record vr
JOIN schedule_detail sd ON vr.schedule_detail_id = sd.id
WHERE vr.volunteer_record_status = 'PENDING'
  AND vr.scheduled_date = :today
  AND sd.end_time < :nowTime

-- 과거 날짜의 PENDING 레코드들
SELECT vr.*
FROM volunteer_record vr
WHERE vr.volunteer_record_status = 'PENDING'
  AND vr.scheduled_date < :today
```

### 2. 일괄 업데이트
```sql
UPDATE volunteer_record vr
SET vr.volunteer_record_status = 'NOT_CONDUCTED'
WHERE vr.id IN (:recordIds)
```

## 🔍 모니터링

### 1. 로그 레벨
- **INFO**: cron job 실행 시작/완료, 업데이트된 레코드 수
- **DEBUG**: 개별 레코드 업데이트 상세 정보
- **ERROR**: cron job 실행 중 오류

### 2. 로그 예시
```
2025-01-30 14:30:00.000 INFO  [cron-1] [VolunteerRecordCronService] [Cron Job] 만료된 PENDING 봉사 기록 업데이트 시작
2025-01-30 14:30:00.100 INFO  [cron-1] [VolunteerRecordCronService] [Cron Job] 만료된 PENDING 봉사 기록 업데이트 완료 - 총 5개 레코드가 NOT_CONDUCTED로 변경됨
```

## ⚠️ 주의사항

### 1. 성능 고려사항
- 대량의 레코드가 있는 경우 일괄 업데이트 사용
- 인덱스 최적화 필요 (scheduled_date, volunteer_record_status, schedule_detail_id)

### 2. 동시성 고려사항
- `@Transactional` 사용으로 데이터 일관성 보장
- 스레드 풀 크기 조정으로 동시 실행 제어

### 3. 오류 처리
- try-catch로 cron job 실패 시 전체 애플리케이션 영향 방지
- 로그를 통한 오류 추적 가능

## 🧪 테스트

### 1. 단위 테스트
```java
@Test
void testUpdateExpiredPendingRecords() {
    // 테스트 데이터 준비
    // cron job 실행
    // 결과 검증
}
```

### 2. 통합 테스트
```java
@Test
void testCronJobIntegration() {
    // 실제 데이터베이스에서 테스트
    // API 호출로 수동 실행 테스트
}
```

## 🔧 커스터마이징

### 1. 실행 주기 변경
```java
@Scheduled(cron = "0 */15 * * * *") // 15분마다
@Scheduled(cron = "0 0 */1 * * *")  // 매시간
```

### 2. 조건 추가
```java
// 특정 매칭만 처리
List<VolunteerRecord> expiredRecords = volunteerRecordRepository
    .findPendingRecordsWithExpiredEndTimeAndMatching(today, nowTime, specificMatching);
```

### 3. 알림 기능 추가
```java
// Slack, 이메일 등으로 알림 발송
notificationService.sendExpiredRecordsNotification(expiredRecords);
```
