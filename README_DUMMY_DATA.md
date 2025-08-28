# VolunteerRecord 더미 데이터 삽입 가이드

## 📋 개요
이 문서는 `VolunteerRecord` 및 관련 테이블들에 더미 데이터를 삽입하는 방법을 설명합니다.

## 📁 파일 설명

### 1. `dummy_data.sql` - 상세한 더미 데이터
- **용도**: 완전한 테스트 데이터가 필요한 경우
- **내용**: 
  - 다양한 상태의 봉사 기록 (COMPLETE, ABSENT, NOT_CONDUCTED)
  - 상세한 리포트 데이터 (건강, 심리, 의견)
  - 다중 통화 이력 (한 봉사 기록에 여러 통화)
  - 여러 매칭에 대한 데이터
  - 데이터 확인용 쿼리 포함

### 2. `simple_dummy_data.sql` - 간단한 더미 데이터
- **용도**: 빠른 테스트가 필요한 경우
- **내용**:
  - 기본적인 상태 변경
  - 간단한 리포트 데이터
  - 단일 통화 이력
  - 기본 확인 쿼리

## 🚀 실행 방법

### 방법 1: MySQL Workbench 또는 phpMyAdmin 사용
1. 데이터베이스에 연결
2. SQL 편집기 열기
3. 원하는 SQL 파일 내용을 복사하여 붙여넣기
4. 실행

### 방법 2: MySQL 명령줄 사용
```bash
# 데이터베이스 연결
mysql -u username -p database_name

# SQL 파일 실행
source /path/to/dummy_data.sql;
```

### 방법 3: Spring Boot 애플리케이션에서 실행
```bash
# 애플리케이션 실행 후 데이터베이스에 직접 연결하여 실행
```

## 📊 삽입되는 데이터 유형

### 1. VolunteerRecord 상태별 분포
- **COMPLETE**: 완료된 봉사 기록 (통화 시간, 리포트 포함)
- **ABSENT**: 부재중 (통화 시도했지만 연결 안됨)
- **NOT_CONDUCTED**: 미실시 (통화 시도하지 않음)
- **PENDING**: 예정 (기본 상태, 변경하지 않음)

### 2. 통화 시간 분포
- 15분 ~ 40분 사이의 다양한 통화 시간
- 부재중: 0초
- 미실시: NULL

### 3. 리포트 데이터
- **건강 상태**: GOOD, NORMAL, BAD
- **심리 상태**: GOOD, NORMAL, BAD
- **의견**: 실제 상황에 맞는 상세한 의견

## ⚠️ 주의사항

### 1. ID 확인
- SQL 쿼리에서 사용하는 ID들이 실제 데이터베이스에 존재하는지 확인
- 존재하지 않는 ID에 대해 UPDATE/INSERT 시 오류 발생 가능

### 2. 외래 키 제약 조건
- `volunteer_record_id`가 실제 존재하는 VolunteerRecord의 ID인지 확인
- `matching_id`, `schedule_detail_id` 등도 유효한 값인지 확인

### 3. 날짜 데이터
- `scheduled_date`는 실제 스케줄과 일치하는지 확인
- `start_time`은 `scheduled_date`와 일치하는 날짜로 설정

## 🔧 커스터마이징

### ID 변경
```sql
-- 실제 존재하는 ID로 변경
UPDATE volunteer_record SET ... WHERE id = [실제_ID];
```

### 날짜 변경
```sql
-- 현재 날짜에 맞게 조정
UPDATE volunteer_record SET start_time = '2025-01-30 14:00:00' WHERE id = 1;
```

### 매칭 ID 변경
```sql
-- 실제 매칭 ID로 변경
UPDATE volunteer_record SET matching_id = [실제_매칭_ID] WHERE id = 1;
```

## 📈 데이터 확인 쿼리

### 기본 확인
```sql
-- 상태별 봉사 기록 수
SELECT volunteer_record_status, COUNT(*) as count 
FROM volunteer_record 
GROUP BY volunteer_record_status;

-- 완료된 봉사 기록 조회
SELECT * FROM volunteer_record 
WHERE volunteer_record_status = 'COMPLETE' 
ORDER BY scheduled_date DESC;
```

### 상세 확인
```sql
-- 봉사 기록과 리포트 조인
SELECT vr.*, r.health, r.mentality, r.opinion
FROM volunteer_record vr
LEFT JOIN report r ON vr.id = r.volunteer_record_id
WHERE vr.volunteer_record_status = 'COMPLETE';

-- 통화 이력 확인
SELECT * FROM call_history 
ORDER BY volunteer_record_id, start_time;
```

## 🧹 데이터 정리 (필요시)

```sql
-- 더미 데이터 삭제 (주의: 실제 데이터도 삭제됨)
DELETE FROM call_history WHERE volunteer_record_id IN (1,2,3,4,5);
DELETE FROM report WHERE volunteer_record_id IN (1,2,3,4,5);
UPDATE volunteer_record SET 
    volunteer_record_status = 'PENDING',
    start_time = NULL,
    total_call_time = NULL
WHERE id IN (1,2,3,4,5);
```
