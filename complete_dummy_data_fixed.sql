-- =====================================================
-- 완전한 더미 데이터 생성 SQL (외래 키 제약 조건 해결 버전)
-- 기관 → 어르신/봉사자 등록 → 매칭 → 봉사 기록까지 전체 플로우
-- =====================================================

-- =====================================================
-- 0. 기존 데이터 정리
-- =====================================================

-- 기존 데이터가 있다면 삭제 (순서 주의: 외래 키 제약 조건 때문에 역순으로 삭제)
DELETE FROM call_history;
DELETE FROM report;
DELETE FROM volunteer_record;
DELETE FROM schedule_detail;
DELETE FROM schedule;
DELETE FROM matching;
DELETE FROM senior;
DELETE FROM volunteer;
DELETE FROM organization;
DELETE FROM user;

-- AUTO_INCREMENT 초기화
ALTER TABLE user AUTO_INCREMENT = 1;
ALTER TABLE organization AUTO_INCREMENT = 1;
ALTER TABLE volunteer AUTO_INCREMENT = 1;
ALTER TABLE senior AUTO_INCREMENT = 1;
ALTER TABLE matching AUTO_INCREMENT = 1;
ALTER TABLE schedule AUTO_INCREMENT = 1;
ALTER TABLE schedule_detail AUTO_INCREMENT = 1;
ALTER TABLE volunteer_record AUTO_INCREMENT = 1;
ALTER TABLE report AUTO_INCREMENT = 1;
ALTER TABLE call_history AUTO_INCREMENT = 1;

-- =====================================================
-- 1. 기관 계정 생성
-- =====================================================

-- 기관 사용자 생성
INSERT INTO user (username, password, role, status, created_at, updated_at) VALUES
('test_org', 'test_org', 'ORG', 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- 기관 정보 생성 (방금 생성된 user의 id 사용)
INSERT INTO organization (name, manager, manager_phone, user_id, status, created_at, updated_at) VALUES
('광진노인복지센터', '김길동', '02-466-6242', LAST_INSERT_ID(), 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- =====================================================
-- 2. 봉사자 계정 생성
-- =====================================================

-- 봉사자 사용자 생성
INSERT INTO user (username, password, role, status, created_at, updated_at) VALUES
('test_vol', 'test_vol', 'VOL', 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- 봉사자 정보 생성 (방금 생성된 user의 id 사용)
INSERT INTO volunteer (name, birthday, gender, phone, user_id, status, created_at, updated_at) VALUES
('김재훈', '2003-01-01', 'MALE', '010-9460-1439', LAST_INSERT_ID(), 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- =====================================================
-- 3. 어르신 등록 (기관이 등록)
-- =====================================================

-- 어르신 1: 김순자
INSERT INTO senior (name, birthday, phone, code, notes, organization_id, status, created_at, updated_at) VALUES
('김순자', '1948-01-01', '010-9460-1439', 'SENIOR001', '치매초기, 약을 자주 까먹으심', (SELECT id FROM organization LIMIT 1), 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- 어르신 2: 박철수
INSERT INTO senior (name, birthday, phone, code, notes, organization_id, status, created_at, updated_at) VALUES
('박철수', '1953-01-01', '010-9460-1439', 'SENIOR002', '화가 많으심, 알콜의존증이 조금 있어서 술을 좀 덜드시게 유도 해주세요.', (SELECT id FROM organization LIMIT 1), 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- =====================================================
-- 4. 매칭 생성
-- =====================================================

-- 김순자와 김재훈 매칭 (화,금 19:00-20:00)
INSERT INTO matching (volunteer_id, senior_id, matching_status, status, created_at, updated_at) VALUES
((SELECT id FROM volunteer LIMIT 1), (SELECT id FROM senior WHERE name = '김순자' LIMIT 1), 'ACTIVE', 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- 박철수와 김재훈 매칭 (월,수 11:00-12:00)
INSERT INTO matching (volunteer_id, senior_id, matching_status, status, created_at, updated_at) VALUES
((SELECT id FROM volunteer LIMIT 1), (SELECT id FROM senior WHERE name = '박철수' LIMIT 1), 'ACTIVE', 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- =====================================================
-- 5. 스케줄 생성 (매칭과 연결)
-- =====================================================

-- 김순자 스케줄 (2025-08-01 ~ 2025-09-01)
INSERT INTO schedule (start_date, end_date, matching_id, status, created_at, updated_at) VALUES
('2025-08-01', '2025-09-01', (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1), 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- 박철수 스케줄 (2025-08-01 ~ 2025-09-01)
INSERT INTO schedule (start_date, end_date, matching_id, status, created_at, updated_at) VALUES
('2025-08-01', '2025-09-01', (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1), 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- =====================================================
-- 6. 스케줄 상세 정보 생성
-- =====================================================

-- 화요일, 금요일 19:00-20:00 스케줄 상세 (김순자)
INSERT INTO schedule_detail (day, start_time, end_time, schedule_id, status, created_at, updated_at) VALUES
('TUE', '19:00:00', '20:00:00', (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1), 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00'),
('FRI', '19:00:00', '20:00:00', (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1), 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- 월요일, 수요일 11:00-12:00 스케줄 상세 (박철수)
INSERT INTO schedule_detail (day, start_time, end_time, schedule_id, status, created_at, updated_at) VALUES
('MON', '11:00:00', '12:00:00', (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1) LIMIT 1), 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00'),
('WED', '11:00:00', '12:00:00', (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1) LIMIT 1), 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00');

-- =====================================================
-- 7. 봉사 기록 생성 (8월 한 달간)
-- =====================================================

-- 김순자 봉사 기록 (화,금 19:00-20:00)
INSERT INTO volunteer_record (volunteer_record_status, scheduled_date, scheduled_time, schedule_detail_id, matching_id, status, created_at, updated_at) VALUES
-- 8월 1주차
('COMPLETE', '2025-08-01', '19:00:00', (SELECT id FROM schedule_detail WHERE day = 'TUE' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1), 'Y', '2025-08-01 00:00:00', '2025-08-01 00:00:00'),
('ABSENT', '2025-08-02', '19:00:00', (SELECT id FROM schedule_detail WHERE day = 'FRI' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1), 'Y', '2025-08-02 00:00:00', '2025-08-02 00:00:00'),

-- 8월 2주차
('COMPLETE', '2025-08-05', '19:00:00', (SELECT id FROM schedule_detail WHERE day = 'TUE' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1), 'Y', '2025-08-05 00:00:00', '2025-08-05 00:00:00'),
('NOT_CONDUCTED', '2025-08-09', '19:00:00', (SELECT id FROM schedule_detail WHERE day = 'FRI' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1), 'Y', '2025-08-09 00:00:00', '2025-08-09 00:00:00'),

-- 8월 3주차
('COMPLETE', '2025-08-12', '19:00:00', (SELECT id FROM schedule_detail WHERE day = 'TUE' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1), 'Y', '2025-08-12 00:00:00', '2025-08-12 00:00:00'),
('COMPLETE', '2025-08-16', '19:00:00', (SELECT id FROM schedule_detail WHERE day = 'FRI' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1), 'Y', '2025-08-16 00:00:00', '2025-08-16 00:00:00'),

-- 8월 4주차
('ABSENT', '2025-08-19', '19:00:00', (SELECT id FROM schedule_detail WHERE day = 'TUE' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1), 'Y', '2025-08-19 00:00:00', '2025-08-19 00:00:00'),
('COMPLETE', '2025-08-23', '19:00:00', (SELECT id FROM schedule_detail WHERE day = 'FRI' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1), 'Y', '2025-08-23 00:00:00', '2025-08-23 00:00:00'),

-- 8월 5주차
('COMPLETE', '2025-08-26', '19:00:00', (SELECT id FROM schedule_detail WHERE day = 'TUE' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1), 'Y', '2025-08-26 00:00:00', '2025-08-26 00:00:00'),
('NOT_CONDUCTED', '2025-08-30', '19:00:00', (SELECT id FROM schedule_detail WHERE day = 'FRI' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '김순자' LIMIT 1) LIMIT 1), 'Y', '2025-08-30 00:00:00', '2025-08-30 00:00:00');

-- 박철수 봉사 기록 (월,수 11:00-12:00)
INSERT INTO volunteer_record (volunteer_record_status, scheduled_date, scheduled_time, schedule_detail_id, matching_id, status, created_at, updated_at) VALUES
-- 8월 1주차
('COMPLETE', '2025-08-04', '11:00:00', (SELECT id FROM schedule_detail WHERE day = 'MON' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1), 'Y', '2025-08-04 00:00:00', '2025-08-04 00:00:00'),
('ABSENT', '2025-08-06', '11:00:00', (SELECT id FROM schedule_detail WHERE day = 'WED' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1), 'Y', '2025-08-06 00:00:00', '2025-08-06 00:00:00'),

-- 8월 2주차
('NOT_CONDUCTED', '2025-08-11', '11:00:00', (SELECT id FROM schedule_detail WHERE day = 'MON' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1), 'Y', '2025-08-11 00:00:00', '2025-08-11 00:00:00'),
('COMPLETE', '2025-08-13', '11:00:00', (SELECT id FROM schedule_detail WHERE day = 'WED' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1), 'Y', '2025-08-13 00:00:00', '2025-08-13 00:00:00'),

-- 8월 3주차
('COMPLETE', '2025-08-18', '11:00:00', (SELECT id FROM schedule_detail WHERE day = 'MON' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1), 'Y', '2025-08-18 00:00:00', '2025-08-18 00:00:00'),
('ABSENT', '2025-08-20', '11:00:00', (SELECT id FROM schedule_detail WHERE day = 'WED' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1), 'Y', '2025-08-20 00:00:00', '2025-08-20 00:00:00'),

-- 8월 4주차
('COMPLETE', '2025-08-25', '11:00:00', (SELECT id FROM schedule_detail WHERE day = 'MON' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1), 'Y', '2025-08-25 00:00:00', '2025-08-25 00:00:00'),
('NOT_CONDUCTED', '2025-08-27', '11:00:00', (SELECT id FROM schedule_detail WHERE day = 'WED' AND schedule_id = (SELECT id FROM schedule WHERE matching_id = (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1) LIMIT 1) LIMIT 1), (SELECT id FROM matching WHERE senior_id = (SELECT id FROM senior WHERE name = '박철수' LIMIT 1) LIMIT 1), 'Y', '2025-08-27 00:00:00', '2025-08-27 00:00:00');

-- =====================================================
-- 8. 봉사 기록 상세 정보 업데이트
-- =====================================================

-- 김순자 봉사 기록 상세 업데이트 (COMPLETE인 경우만)
UPDATE volunteer_record SET 
    start_time = '2025-08-01 19:00:00',
    total_call_time = 1800  -- 30분
WHERE id = 1 AND volunteer_record_status = 'COMPLETE';

UPDATE volunteer_record SET 
    start_time = NULL,
    total_call_time = NULL  -- ABSENT
WHERE id = 2 AND volunteer_record_status = 'ABSENT';

UPDATE volunteer_record SET 
    start_time = '2025-08-05 19:00:00',
    total_call_time = 2100  -- 35분
WHERE id = 3 AND volunteer_record_status = 'COMPLETE';

UPDATE volunteer_record SET 
    start_time = NULL,
    total_call_time = NULL  -- NOT_CONDUCTED
WHERE id = 4 AND volunteer_record_status = 'NOT_CONDUCTED';

UPDATE volunteer_record SET 
    start_time = '2025-08-12 19:00:00',
    total_call_time = 2400  -- 40분
WHERE id = 5 AND volunteer_record_status = 'COMPLETE';

UPDATE volunteer_record SET 
    start_time = '2025-08-16 19:00:00',
    total_call_time = 900   -- 15분
WHERE id = 6 AND volunteer_record_status = 'COMPLETE';

UPDATE volunteer_record SET 
    start_time = NULL,
    total_call_time = NULL  -- ABSENT
WHERE id = 7 AND volunteer_record_status = 'ABSENT';

UPDATE volunteer_record SET 
    start_time = '2025-08-23 19:00:00',
    total_call_time = 1500  -- 25분
WHERE id = 8 AND volunteer_record_status = 'COMPLETE';

UPDATE volunteer_record SET 
    start_time = '2025-08-26 19:00:00',
    total_call_time = 2100  -- 35분
WHERE id = 9 AND volunteer_record_status = 'COMPLETE';

UPDATE volunteer_record SET 
    start_time = NULL,
    total_call_time = NULL  -- NOT_CONDUCTED
WHERE id = 10 AND volunteer_record_status = 'NOT_CONDUCTED';

-- 박철수 봉사 기록 상세 업데이트 (COMPLETE인 경우만)
UPDATE volunteer_record SET 
    start_time = '2025-08-04 11:00:00',
    total_call_time = 1800  -- 30분
WHERE id = 11 AND volunteer_record_status = 'COMPLETE';

UPDATE volunteer_record SET 
    start_time = NULL,
    total_call_time = NULL  -- ABSENT
WHERE id = 12 AND volunteer_record_status = 'ABSENT';

UPDATE volunteer_record SET 
    start_time = NULL,
    total_call_time = NULL  -- NOT_CONDUCTED
WHERE id = 13 AND volunteer_record_status = 'NOT_CONDUCTED';

UPDATE volunteer_record SET 
    start_time = '2025-08-13 11:00:00',
    total_call_time = 1200  -- 20분
WHERE id = 14 AND volunteer_record_status = 'COMPLETE';

UPDATE volunteer_record SET 
    start_time = '2025-08-18 11:00:00',
    total_call_time = 2400  -- 40분
WHERE id = 15 AND volunteer_record_status = 'COMPLETE';

UPDATE volunteer_record SET 
    start_time = NULL,
    total_call_time = NULL  -- ABSENT
WHERE id = 16 AND volunteer_record_status = 'ABSENT';

UPDATE volunteer_record SET 
    start_time = '2025-08-25 11:00:00',
    total_call_time = 1800  -- 30분
WHERE id = 17 AND volunteer_record_status = 'COMPLETE';

UPDATE volunteer_record SET 
    start_time = NULL,
    total_call_time = NULL  -- NOT_CONDUCTED
WHERE id = 18 AND volunteer_record_status = 'NOT_CONDUCTED';

-- =====================================================
-- 9. 리포트 데이터 생성
-- =====================================================

-- 김순자 리포트 (모든 봉사 기록에 대해)
INSERT INTO report (health, mentality, opinion, volunteer_record_id, status, created_at, updated_at) VALUES
-- COMPLETE인 경우
('GOOD', 'GOOD', '오늘따라 기분이 좋아 보이셨습니다. 식사도 잘 하셨고, 어제는 경로당에도 다녀오셨다고 합니다. 특별한 건강문제는 없어보입니다.', 1, 'Y', '2025-08-01 19:30:00', '2025-08-01 19:30:00'),
('GOOD', 'NORMAL', '건강상태는 좋으시지만 오늘따라 조용하셨습니다. 무슨 일이 있으신지 걱정되지만 직접적으로 말씀하지는 않으셨습니다. 오래 통화해주셔서 감사했습니다.', 3, 'Y', '2025-08-05 19:35:00', '2025-08-05 19:35:00'),
('GOOD', 'GOOD', '오늘은 정말 오래 통화하셨습니다. 가족 이야기부터 옛날 이야기까지 정말 재미있게 들었습니다. 건강하시고 기분도 좋으시다고 하셔서 다행입니다.', 5, 'Y', '2025-08-12 19:40:00', '2025-08-12 19:40:00'),
('NORMAL', 'NORMAL', '오늘은 짧게 통화하셨습니다. 바쁘신 것 같아서 일찍 끊어드렸습니다. 건강하시다고 하셨습니다.', 6, 'Y', '2025-08-16 19:15:00', '2025-08-16 19:15:00'),
('NORMAL', 'GOOD', '오늘은 기분이 좋으셨습니다. 손자분이 방문하신다고 하셔서 기대에 찬 하루를 보내고 계셨습니다.', 8, 'Y', '2025-08-23 19:25:00', '2025-08-23 19:25:00'),
('GOOD', 'NORMAL', '오늘은 오래 통화하셨습니다. 건강상태는 좋으시지만 가족분들과 연락이 안 되셔서 걱정되신 것 같습니다. 내일은 연락이 될 것 같다고 위로드렸습니다.', 9, 'Y', '2025-08-26 19:35:00', '2025-08-26 19:35:00'),

-- ABSENT인 경우
('NORMAL', 'NORMAL', '오늘은 부재중이셨습니다. 전화를 여러 번 걸어봤지만 받지 않으셨습니다. 내일 다시 연락드리겠습니다.', 2, 'Y', '2025-08-02 19:30:00', '2025-08-02 19:30:00'),
('NORMAL', 'NORMAL', '오늘도 부재중이셨습니다. 아마 외출하신 것 같습니다. 다음 주에 다시 연락드리겠습니다.', 7, 'Y', '2025-08-19 19:30:00', '2025-08-19 19:30:00');

-- 박철수 리포트 (모든 봉사 기록에 대해)
INSERT INTO report (health, mentality, opinion, volunteer_record_id, status, created_at, updated_at) VALUES
-- COMPLETE인 경우
('GOOD', 'NORMAL', '오늘은 평소와 비슷하게 통화하셨습니다. 건강하시고 특별한 문제는 없으시다고 하셨습니다.', 11, 'Y', '2025-08-04 11:30:00', '2025-08-04 11:30:00'),
('NORMAL', 'NORMAL', '오늘은 짧게 통화하셨습니다. 건강하시고 특별한 문제는 없으시다고 하셨습니다.', 14, 'Y', '2025-08-13 11:20:00', '2025-08-13 11:20:00'),
('GOOD', 'GOOD', '오늘은 정말 오래 통화하셨습니다. 옛날 이야기부터 현재 이야기까지 정말 재미있게 들었습니다. 건강하시고 기분도 좋으시다고 하셔서 다행입니다.', 15, 'Y', '2025-08-18 11:40:00', '2025-08-18 11:40:00'),
('GOOD', 'GOOD', '오늘은 기분이 좋으셨습니다. 식사도 잘 하시고 운동도 하셨다고 합니다. 건강하시고 활기차 보이셨습니다.', 17, 'Y', '2025-08-25 11:30:00', '2025-08-25 11:30:00'),

-- ABSENT인 경우
('NORMAL', 'NORMAL', '오늘은 부재중이셨습니다. 전화를 여러 번 걸어봤지만 받지 않으셨습니다. 내일 다시 연락드리겠습니다.', 12, 'Y', '2025-08-06 11:30:00', '2025-08-06 11:30:00'),
('NORMAL', 'NORMAL', '오늘도 부재중이셨습니다. 아마 외출하신 것 같습니다. 다음 주에 다시 연락드리겠습니다.', 16, 'Y', '2025-08-20 11:30:00', '2025-08-20 11:30:00');

-- =====================================================
-- 10. 통화 이력 데이터 생성
-- =====================================================

-- 통화 이력 (모든 봉사 기록에 대해)
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
-- 김순자 COMPLETE인 경우
('2025-08-01 19:00:00', 1800, 1, 'Y', '2025-08-01 19:00:00', '2025-08-01 19:00:00'),   -- 30분
('2025-08-05 19:00:00', 2100, 3, 'Y', '2025-08-05 19:00:00', '2025-08-05 19:00:00'),   -- 35분
('2025-08-12 19:00:00', 2400, 5, 'Y', '2025-08-12 19:00:00', '2025-08-12 19:00:00'),   -- 40분
('2025-08-16 19:00:00', 900, 6, 'Y', '2025-08-16 19:00:00', '2025-08-16 19:00:00'),    -- 15분
('2025-08-23 19:00:00', 1500, 8, 'Y', '2025-08-23 19:00:00', '2025-08-23 19:00:00'),   -- 25분
('2025-08-26 19:00:00', 2100, 9, 'Y', '2025-08-26 19:00:00', '2025-08-26 19:00:00'),   -- 35분

-- 김순자 ABSENT인 경우 (전화 시도했지만 받지 못함)
('2025-08-02 19:00:00', 0, 2, 'Y', '2025-08-02 19:00:00', '2025-08-02 19:00:00'),      -- 전화 시도했지만 받지 못함
('2025-08-19 19:00:00', 0, 7, 'Y', '2025-08-19 19:00:00', '2025-08-19 19:00:00'),      -- 전화 시도했지만 받지 못함

-- 박철수 COMPLETE인 경우
('2025-08-04 11:00:00', 1800, 11, 'Y', '2025-08-04 11:00:00', '2025-08-04 11:00:00'),  -- 30분
('2025-08-13 11:00:00', 1200, 14, 'Y', '2025-08-13 11:00:00', '2025-08-13 11:00:00'),  -- 20분
('2025-08-18 11:00:00', 2400, 15, 'Y', '2025-08-18 11:00:00', '2025-08-18 11:00:00'),  -- 40분
('2025-08-25 11:00:00', 1800, 17, 'Y', '2025-08-25 11:00:00', '2025-08-25 11:00:00'),  -- 30분

-- 박철수 ABSENT인 경우 (전화 시도했지만 받지 못함)
('2025-08-06 11:00:00', 0, 12, 'Y', '2025-08-06 11:00:00', '2025-08-06 11:00:00'),      -- 전화 시도했지만 받지 못함
('2025-08-20 11:00:00', 0, 16, 'Y', '2025-08-20 11:00:00', '2025-08-20 11:00:00');      -- 전화 시도했지만 받지 못함



-- =====================================================
-- 11. 데이터 확인 쿼리
-- =====================================================

-- 전체 매칭 현황 확인
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

-- 봉사 기록 통계
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

-- 어르신별 봉사 현황
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

-- 봉사자별 봉사 현황
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

-- 완료된 봉사 기록과 리포트 조회
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
