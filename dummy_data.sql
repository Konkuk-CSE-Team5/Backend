-- =====================================================
-- VolunteerRecord 및 관련 테이블 더미 데이터 삽입
-- =====================================================

-- 1. VolunteerRecord 테이블에 더미 데이터 삽입
-- (기존 PENDING 상태의 레코드들을 다양한 상태로 업데이트)

-- 완료된 봉사 기록들 (COMPLETE)
UPDATE volunteer_record 
SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-01-20 14:00:00',
    total_call_time = 1800  -- 30분 (초 단위)
WHERE id = 1;

UPDATE volunteer_record 
SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-01-22 15:30:00',
    total_call_time = 1200  -- 20분
WHERE id = 2;

UPDATE volunteer_record 
SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-01-24 16:45:00',
    total_call_time = 2100  -- 35분
WHERE id = 3;

UPDATE volunteer_record 
SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-01-27 10:15:00',
    total_call_time = 900   -- 15분
WHERE id = 4;

UPDATE volunteer_record 
SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-01-29 19:20:00',
    total_call_time = 2400  -- 40분
WHERE id = 5;

-- 부재중인 봉사 기록들 (ABSENT)
UPDATE volunteer_record 
SET 
    volunteer_record_status = 'ABSENT',
    start_time = '2025-01-21 14:00:00',
    total_call_time = 0     -- 부재중이므로 0초
WHERE id = 6;

UPDATE volunteer_record 
SET 
    volunteer_record_status = 'ABSENT',
    start_time = '2025-01-23 15:30:00',
    total_call_time = 0
WHERE id = 7;

-- 미실시 봉사 기록들 (NOT_CONDUCTED)
UPDATE volunteer_record 
SET 
    volunteer_record_status = 'NOT_CONDUCTED',
    start_time = NULL,      -- 미실시이므로 start_time 없음
    total_call_time = NULL  -- 미실시이므로 total_call_time 없음
WHERE id = 8;

UPDATE volunteer_record 
SET 
    volunteer_record_status = 'NOT_CONDUCTED',
    start_time = NULL,
    total_call_time = NULL
WHERE id = 9;

-- =====================================================
-- Report 테이블에 더미 데이터 삽입
-- =====================================================

-- 완료된 봉사 기록들에 대한 리포트 생성
INSERT INTO report (health, mentality, opinion, volunteer_record_id, status, created_at, updated_at) VALUES
('GOOD', 'GOOD', '오늘따라 기분이 좋아 보이셨습니다. 식사도 잘 하셨고, 어제는 경로당에도 다녀오셨다고 합니다. 특별한 건강문제는 없어보입니다.', 1, 'Y', '2025-01-20 14:30:00', '2025-01-20 14:30:00'),
('NORMAL', 'GOOD', '오늘은 평소와 비슷한 컨디션이셨습니다. 식사는 잘 하셨지만 조금 피곤해 보이셨습니다. 내일은 더 좋아지실 것 같습니다.', 2, 'Y', '2025-01-22 15:45:00', '2025-01-22 15:45:00'),
('GOOD', 'NORMAL', '건강상태는 좋으시지만 오늘따라 조용하셨습니다. 무슨 일이 있으신지 걱정되지만 직접적으로 말씀하지는 않으셨습니다.', 3, 'Y', '2025-01-24 17:00:00', '2025-01-24 17:00:00'),
('NORMAL', 'BAD', '오늘은 기분이 좋지 않으신 것 같습니다. 아침부터 머리가 아프시다고 하셨고, 식사도 거의 하지 않으셨습니다.', 4, 'Y', '2025-01-27 10:30:00', '2025-01-27 10:30:00'),
('BAD', 'NORMAL', '오늘은 건강상태가 좋지 않으신 것 같습니다. 기침이 심하시고 목소리도 쉬어 보이셨습니다. 병원에 가보시는 것을 권해드렸습니다.', 5, 'Y', '2025-01-29 19:45:00', '2025-01-29 19:45:00');

-- =====================================================
-- CallHistory 테이블에 더미 데이터 삽입
-- =====================================================

-- 첫 번째 봉사 기록의 통화 이력 (30분 통화)
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-01-20 14:00:00', 900, 1, 'Y', '2025-01-20 14:00:00', '2025-01-20 14:00:00'),   -- 15분 통화
('2025-01-20 14:20:00', 900, 1, 'Y', '2025-01-20 14:20:00', '2025-01-20 14:20:00');   -- 15분 통화 (총 30분)

-- 두 번째 봉사 기록의 통화 이력 (20분 통화)
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-01-22 15:30:00', 1200, 2, 'Y', '2025-01-22 15:30:00', '2025-01-22 15:30:00');   -- 20분 통화

-- 세 번째 봉사 기록의 통화 이력 (35분 통화)
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-01-24 16:45:00', 1800, 3, 'Y', '2025-01-24 16:45:00', '2025-01-24 16:45:00'),   -- 30분 통화
('2025-01-24 17:20:00', 300, 3, 'Y', '2025-01-24 17:20:00', '2025-01-24 17:20:00');    -- 5분 통화 (총 35분)

-- 네 번째 봉사 기록의 통화 이력 (15분 통화)
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-01-27 10:15:00', 900, 4, 'Y', '2025-01-27 10:15:00', '2025-01-27 10:15:00');    -- 15분 통화

-- 다섯 번째 봉사 기록의 통화 이력 (40분 통화)
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-01-29 19:20:00', 2400, 5, 'Y', '2025-01-29 19:20:00', '2025-01-29 19:20:00'),   -- 40분 통화

-- 부재중인 봉사 기록의 통화 이력 (0초)
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-01-21 14:00:00', 0, 6, 'Y', '2025-01-21 14:00:00', '2025-01-21 14:00:00'),      -- 부재중
('2025-01-23 15:30:00', 0, 7, 'Y', '2025-01-23 15:30:00', '2025-01-23 15:30:00');      -- 부재중

-- =====================================================
-- 추가 더미 데이터 (다른 매칭들)
-- =====================================================

-- 다른 매칭의 완료된 봉사 기록들
UPDATE volunteer_record 
SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-01-18 09:00:00',
    total_call_time = 1500  -- 25분
WHERE id = 10;

UPDATE volunteer_record 
SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-01-19 11:30:00',
    total_call_time = 1800  -- 30분
WHERE id = 11;

UPDATE volunteer_record 
SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-01-25 13:45:00',
    total_call_time = 1200  -- 20분
WHERE id = 12;

-- 해당 리포트들
INSERT INTO report (health, mentality, opinion, volunteer_record_id, status, created_at, updated_at) VALUES
('GOOD', 'GOOD', '오늘은 아침부터 기분이 좋으셨습니다. 운동도 하시고 식사도 맛있게 하셨다고 합니다.', 10, 'Y', '2025-01-18 09:25:00', '2025-01-18 09:25:00'),
('NORMAL', 'NORMAL', '평소와 비슷한 하루를 보내셨습니다. 특별한 문제는 없으시고 건강하시다고 합니다.', 11, 'Y', '2025-01-19 11:55:00', '2025-01-19 11:55:00'),
('GOOD', 'BAD', '건강상태는 좋으시지만 오늘따라 우울해 보이셨습니다. 가족분들과 연락이 안 되셔서 걱정되신 것 같습니다.', 12, 'Y', '2025-01-25 14:10:00', '2025-01-25 14:10:00');

-- 해당 통화 이력들
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-01-18 09:00:00', 1500, 10, 'Y', '2025-01-18 09:00:00', '2025-01-18 09:00:00'),   -- 25분 통화
('2025-01-19 11:30:00', 1800, 11, 'Y', '2025-01-19 11:30:00', '2025-01-19 11:30:00'),   -- 30분 통화
('2025-01-25 13:45:00', 1200, 12, 'Y', '2025-01-25 13:45:00', '2025-01-25 13:45:00');   -- 20분 통화

-- =====================================================
-- 최근 날짜의 더미 데이터 (테스트용)
-- =====================================================

-- 오늘 날짜의 완료된 봉사 기록
UPDATE volunteer_record 
SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-01-30 14:00:00',
    total_call_time = 1800  -- 30분
WHERE id = 13;

-- 어제 날짜의 부재중 봉사 기록
UPDATE volunteer_record 
SET 
    volunteer_record_status = 'ABSENT',
    start_time = '2025-01-29 15:30:00',
    total_call_time = 0
WHERE id = 14;

-- 해당 리포트
INSERT INTO report (health, mentality, opinion, volunteer_record_id, status, created_at, updated_at) VALUES
('GOOD', 'GOOD', '오늘은 정말 기분이 좋으셨습니다. 손자분이 방문하신다고 하셔서 기대에 찬 하루를 보내고 계셨습니다.', 13, 'Y', '2025-01-30 14:30:00', '2025-01-30 14:30:00');

-- 해당 통화 이력
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-01-30 14:00:00', 1800, 13, 'Y', '2025-01-30 14:00:00', '2025-01-30 14:00:00'),   -- 30분 통화
('2025-01-29 15:30:00', 0, 14, 'Y', '2025-01-29 15:30:00', '2025-01-29 15:30:00');      -- 부재중

-- =====================================================
-- 데이터 확인용 쿼리
-- =====================================================

-- 완료된 봉사 기록 조회
SELECT 
    vr.id,
    vr.volunteer_record_status,
    vr.scheduled_date,
    vr.scheduled_time,
    vr.start_time,
    vr.total_call_time,
    r.health,
    r.mentality,
    r.opinion
FROM volunteer_record vr
LEFT JOIN report r ON vr.id = r.volunteer_record_id
WHERE vr.volunteer_record_status = 'COMPLETE'
ORDER BY vr.scheduled_date DESC, vr.scheduled_time DESC;

-- 통화 이력 조회
SELECT 
    ch.id,
    ch.start_time,
    ch.call_time,
    ch.volunteer_record_id
FROM call_history ch
ORDER BY ch.volunteer_record_id, ch.start_time;

-- 매칭별 봉사 기록 통계
SELECT 
    vr.matching_id,
    COUNT(*) as total_records,
    SUM(CASE WHEN vr.volunteer_record_status = 'COMPLETE' THEN 1 ELSE 0 END) as completed_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'ABSENT' THEN 1 ELSE 0 END) as absent_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'NOT_CONDUCTED' THEN 1 ELSE 0 END) as not_conducted_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'PENDING' THEN 1 ELSE 0 END) as pending_count,
    SUM(COALESCE(vr.total_call_time, 0)) as total_call_time_seconds
FROM volunteer_record vr
GROUP BY vr.matching_id
ORDER BY vr.matching_id;
