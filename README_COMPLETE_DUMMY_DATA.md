# 완전한 더미 데이터 생성 가이드

## 📋 개요
기관 → 어르신/봉사자 등록 → 매칭 → 봉사 기록까지 전체 플로우를 포함한 완전한 더미 데이터를 생성하는 SQL 파일입니다.

## 🔧 생성되는 데이터

### 1. 계정 정보
- **기관**: 광진노인복지센터 (test_org / test_org) - Role: ORG
- **봉사자**: 김재훈 (test_vol / test_vol) - Role: VOL
- **어르신**: 김순자, 박철수

### 2. 매칭 정보
- **매칭 1**: 김재훈 ↔ 김순자 (화,금 19:00-20:00)
- **매칭 2**: 김재훈 ↔ 박철수 (월,수 11:00-12:00)

### 3. 봉사 기록
- **김순자**: 8월 한 달간 화,금요일 10회 봉사 (모두 완료)
- **박철수**: 8월 한 달간 월,수요일 8회 봉사 (모두 완료)

## 🚀 사용 방법

### 1. SQL 파일 실행
```bash
# MySQL Workbench에서 실행
source /path/to/complete_dummy_data.sql

# 또는 명령줄에서 실행
mysql -u username -p database_name < complete_dummy_data.sql
```

### 2. 데이터 확인
SQL 파일 마지막에 포함된 확인 쿼리들을 실행하여 데이터가 정상적으로 생성되었는지 확인할 수 있습니다.

## 📊 생성되는 데이터 상세

### 계정 정보
```json
{
  "기관": {
    "username": "test_org",
    "password": "test_org",
    "name": "광진노인복지센터",
    "manager": "김길동",
    "managerPhone": "02-466-6242",
    "role": "ORG"
  },
  "봉사자": {
    "username": "test_vol",
    "password": "test_vol",
    "name": "김재훈",
    "birthday": "2003-01-01",
    "gender": "MALE",
    "phone": "010-9460-1439",
    "role": "VOL"
  },
  "어르신": [
    {
      "name": "김순자",
      "birthday": "1948-01-01",
      "phone": "010-9460-1439",
      "code": "SENIOR001",
      "notes": "치매초기, 약을 자주 까먹으심"
    },
    {
      "name": "박철수",
      "birthday": "1953-01-01",
      "phone": "010-9460-1439",
      "code": "SENIOR002",
      "notes": "화가 많으심, 알콜의존증이 조금 있어서 술을 좀 덜드시게 유도 해주세요."
    }
  ]
}
```

### 스케줄 정보
```json
{
  "김순자": {
    "workDays": ["화", "금"],
    "workStartTime": "19:00",
    "workEndTime": "20:00",
    "startDate": "2025-08-01",
    "endDate": "2025-09-01"
  },
  "박철수": {
    "workDays": ["월", "수"],
    "workStartTime": "11:00",
    "workEndTime": "12:00",
    "startDate": "2025-08-01",
    "endDate": "2025-09-01"
  }
}
```

## 🔍 데이터 확인 쿼리

### 1. 전체 매칭 현황 확인
```sql
SELECT 
    m.id as matching_id,
    v.name as volunteer_name,
    s.name as senior_name,
    m.matching_status,
    sch.start_date,
    sch.end_date
FROM matching m
JOIN volunteer v ON m.volunteer_id = v.id
JOIN senior s ON m.senior_id = s.id
LEFT JOIN schedule sch ON m.id = sch.matching_id
ORDER BY m.id;
```

### 2. 봉사 기록 통계
```sql
SELECT 
    vr.matching_id,
    v.name as volunteer_name,
    s.name as senior_name,
    COUNT(*) as total_records,
    SUM(CASE WHEN vr.volunteer_record_status = 'COMPLETE' THEN 1 ELSE 0 END) as completed_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'ABSENT' THEN 1 ELSE 0 END) as absent_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'NOT_CONDUCTED' THEN 1 ELSE 0 END) as not_conducted_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'PENDING' THEN 1 ELSE 0 END) as pending_count,
    SUM(vr.total_call_time) as total_call_time_seconds
FROM volunteer_record vr
JOIN matching m ON vr.matching_id = m.id
JOIN volunteer v ON m.volunteer_id = v.id
JOIN senior s ON m.senior_id = s.id
GROUP BY vr.matching_id, v.name, s.name
ORDER BY vr.matching_id;
```

### 3. 어르신별 봉사 현황
```sql
SELECT 
    s.name as senior_name,
    s.notes as senior_notes,
    COUNT(*) as total_records,
    SUM(CASE WHEN vr.volunteer_record_status = 'COMPLETE' THEN 1 ELSE 0 END) as completed_count,
    ROUND(AVG(vr.total_call_time) / 60, 2) as avg_call_time_minutes
FROM volunteer_record vr
JOIN matching m ON vr.matching_id = m.id
JOIN senior s ON m.senior_id = s.id
GROUP BY s.id, s.name, s.notes
ORDER BY s.name;
```

### 4. 봉사자별 봉사 현황
```sql
SELECT 
    v.name as volunteer_name,
    COUNT(*) as total_records,
    SUM(CASE WHEN vr.volunteer_record_status = 'COMPLETE' THEN 1 ELSE 0 END) as completed_count,
    ROUND(AVG(vr.total_call_time) / 60, 2) as avg_call_time_minutes,
    SUM(vr.total_call_time) as total_call_time_seconds
FROM volunteer_record vr
JOIN matching m ON vr.matching_id = m.id
JOIN volunteer v ON m.volunteer_id = v.id
GROUP BY v.id, v.name
ORDER BY v.name;
```

### 5. 완료된 봉사 기록과 리포트 조회
```sql
SELECT 
    vr.id,
    v.name as volunteer_name,
    s.name as senior_name,
    vr.scheduled_date,
    vr.scheduled_time,
    vr.start_time,
    vr.total_call_time,
    r.health,
    r.mentality,
    r.opinion
FROM volunteer_record vr
JOIN matching m ON vr.matching_id = m.id
JOIN volunteer v ON m.volunteer_id = v.id
JOIN senior s ON m.senior_id = s.id
LEFT JOIN report r ON vr.id = r.volunteer_record_id
WHERE vr.volunteer_record_status = 'COMPLETE'
ORDER BY vr.scheduled_date DESC, vr.scheduled_time DESC;
```

## 📈 예상 결과

### 매칭 현황
- 매칭 1: 김재훈 ↔ 김순자 (ACTIVE)
- 매칭 2: 김재훈 ↔ 박철수 (ACTIVE)

### 봉사 기록 통계
- **김순자**: 총 10회, 완료 10회, 평균 통화시간 25.5분
- **박철수**: 총 8회, 완료 8회, 평균 통화시간 26.25분

### 봉사자 통계
- **김재훈**: 총 18회, 완료 18회, 총 통화시간 27,900초 (465분)

## 🧪 테스트 시나리오

### 1. 로그인 테스트
```bash
# 기관 로그인
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "test_org", "password": "test_org", "role": "ORG"}'

# 봉사자 로그인
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "test_vol", "password": "test_vol", "role": "VOL"}'
```

### 2. 봉사 기록 조회 테스트
```bash
# 봉사자 메인 화면 조회
curl -X GET http://localhost:8080/api/volunteers/main \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# 봉사 기록 목록 조회
curl -X GET http://localhost:8080/api/volunteers/records \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# 특정 매칭의 봉사 기록 조회
curl -X GET http://localhost:8080/api/volunteers/records/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 3. 기관 관리 화면 테스트
```bash
# 기관 메인 화면 조회
curl -X GET http://localhost:8080/api/organizations/main \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# 어르신 목록 조회
curl -X GET http://localhost:8080/api/organizations/seniors \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## ⚠️ 주의사항

### 1. 실행 순서
- 기존 데이터가 있다면 백업 후 실행
- 외래 키 제약 조건을 고려하여 순서대로 실행

### 2. 데이터 수정
- 실제 환경에 맞게 날짜, 시간, 이름 등을 수정
- 비밀번호는 BCrypt로 암호화되어 있음 (test_org, test_vol)

### 3. 테스트 환경
- 개발/테스트 환경에서만 사용
- 프로덕션 환경에서는 절대 실행하지 마세요

## 🔧 커스터마이징

### 1. 추가 어르신 등록
```sql
-- 어르신 추가
INSERT INTO senior (name, birthday, contact, notes, organization_id, status, created_at, updated_at) VALUES
('이영자', '1950-01-01', '010-1234-5678', '당뇨, 보조기 사용', 1, 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- 매칭 추가
INSERT INTO matching (volunteer_id, senior_id, matching_status, status, created_at, updated_at) VALUES
(1, 3, 'ACTIVE', 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');
```

### 2. 추가 봉사자 등록
```sql
-- 봉사자 사용자 추가
INSERT INTO users (username, password, role, status, created_at, updated_at) VALUES
('test_vol2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'VOLUNTEER', 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- 봉사자 정보 추가
INSERT INTO volunteer (name, birthday, gender, contact, user_id, status, created_at, updated_at) VALUES
('박영희', '1995-01-01', 'FEMALE', '010-9876-5432', 3, 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');
```

### 3. 다양한 상태의 봉사 기록 추가
```sql
-- ABSENT 상태 추가
UPDATE volunteer_record SET 
    volunteer_record_status = 'ABSENT',
    start_time = '2025-08-15 19:00:00',
    total_call_time = 0
WHERE id = 5;

-- NOT_CONDUCTED 상태 추가
UPDATE volunteer_record SET 
    volunteer_record_status = 'NOT_CONDUCTED',
    start_time = NULL,
    total_call_time = NULL
WHERE id = 6;
```

## 📊 데이터베이스 테이블 구조

### 주요 테이블 및 필드명
```sql
-- 사용자 테이블 (role: ORG, VOL)
user (id, username, password, role, status, created_at, updated_at)

-- 기관 테이블  
organization (id, name, manager, manager_phone, user_id, status, created_at, updated_at)

-- 봉사자 테이블
volunteer (id, name, birthday, gender, phone, user_id, status, created_at, updated_at)

-- 어르신 테이블
senior (id, name, birthday, phone, code, notes, organization_id, status, created_at, updated_at)

-- 매칭 테이블
matching (id, volunteer_id, senior_id, matching_status, status, created_at, updated_at)

-- 스케줄 테이블
schedule (id, start_date, end_date, matching_id, status, created_at, updated_at)

-- 스케줄 상세 테이블
schedule_detail (id, day, start_time, end_time, schedule_id, status, created_at, updated_at)

-- 봉사 기록 테이블
volunteer_record (id, volunteer_record_status, scheduled_date, scheduled_time, start_time, total_call_time, schedule_detail_id, matching_id, status, created_at, updated_at)

-- 리포트 테이블
report (id, health, mentality, opinion, volunteer_record_id, status, created_at, updated_at)

-- 통화 이력 테이블
call_history (id, start_time, call_time, volunteer_record_id, status, created_at, updated_at)
```

### 주요 관계
- `user` ↔ `organization` (1:1)
- `user` ↔ `volunteer` (1:1)  
- `organization` ↔ `senior` (1:N)
- `volunteer` ↔ `matching` (1:N)
- `senior` ↔ `matching` (1:N)
- `matching` ↔ `schedule` (1:1)
- `schedule` ↔ `schedule_detail` (1:N)
- `matching` ↔ `volunteer_record` (1:N)
- `schedule_detail` ↔ `volunteer_record` (1:N)
- `volunteer_record` ↔ `report` (1:1)
- `volunteer_record` ↔ `call_history` (1:N)

## 📚 관련 파일

- `complete_dummy_data.sql`: 완전한 더미 데이터 생성 SQL
- `realistic_dummy_data.sql`: 실제 데이터에 맞춘 봉사 기록 업데이트 SQL
- `simple_dummy_data.sql`: 간단한 더미 데이터 SQL
