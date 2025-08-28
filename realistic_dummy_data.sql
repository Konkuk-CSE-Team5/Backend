-- =====================================================
-- 실제 JSON 데이터에 맞춘 VolunteerRecord 더미 데이터
-- =====================================================

-- 실제 데이터베이스의 JSON 데이터를 기반으로 작성
-- ID 1-27까지의 실제 scheduled_date와 scheduled_time을 정확히 반영

-- =====================================================
-- 1. VolunteerRecord 상태 업데이트 (실제 ID 1-27 사용)
-- =====================================================

-- 매칭 6 (matching_id: 6) - 20:00:00 시간대
-- ID 1: 2025-08-01 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-01 20:00:00',
    total_call_time = 1800  -- 30분
WHERE id = 1;

-- ID 2: 2025-08-05 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-05 20:00:00',
    total_call_time = 1500  -- 25분
WHERE id = 2;

-- ID 3: 2025-08-08 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-08 20:00:00',
    total_call_time = 2100  -- 35분
WHERE id = 3;

-- ID 4: 2025-08-12 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'ABSENT',
    start_time = '2025-08-12 20:00:00',
    total_call_time = 0     -- 부재중
WHERE id = 4;

-- ID 5: 2025-08-15 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-15 20:00:00',
    total_call_time = 1200  -- 20분
WHERE id = 5;

-- ID 6: 2025-08-19 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'NOT_CONDUCTED',
    start_time = NULL,
    total_call_time = NULL  -- 미실시
WHERE id = 6;

-- ID 7: 2025-08-22 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-22 20:00:00',
    total_call_time = 2400  -- 40분
WHERE id = 7;

-- ID 8: 2025-08-26 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-26 20:00:00',
    total_call_time = 900   -- 15분
WHERE id = 8;

-- ID 9: 2025-08-29 20:00:00 (이미 COMPLETE 상태, total_call_time: 3492초)
UPDATE volunteer_record SET 
    start_time = '2025-08-29 20:00:00'
WHERE id = 9;

-- 매칭 5 (matching_id: 5) - 20:00:00 시간대
-- ID 10: 2025-08-04 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-04 20:00:00',
    total_call_time = 1800  -- 30분
WHERE id = 10;

-- ID 11: 2025-08-06 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-06 20:00:00',
    total_call_time = 1500  -- 25분
WHERE id = 11;

-- ID 12: 2025-08-11 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'ABSENT',
    start_time = '2025-08-11 20:00:00',
    total_call_time = 0
WHERE id = 12;

-- ID 13: 2025-08-13 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-13 20:00:00',
    total_call_time = 2100  -- 35분
WHERE id = 13;

-- ID 14: 2025-08-18 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-18 20:00:00',
    total_call_time = 1200  -- 20분
WHERE id = 14;

-- ID 15: 2025-08-20 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'NOT_CONDUCTED',
    start_time = NULL,
    total_call_time = NULL
WHERE id = 15;

-- ID 16: 2025-08-25 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-25 20:00:00',
    total_call_time = 1800  -- 30분
WHERE id = 16;

-- ID 17: 2025-08-27 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-27 20:00:00',
    total_call_time = 2400  -- 40분
WHERE id = 17;

-- ID 18: 2025-09-01 20:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-09-01 20:00:00',
    total_call_time = 1500  -- 25분
WHERE id = 18;

-- 매칭 9 (matching_id: 9) - 04:00:00 시간대
-- ID 19: 2025-08-29 04:00:00 (이미 COMPLETE 상태, total_call_time: 432초)
UPDATE volunteer_record SET 
    start_time = '2025-08-29 04:00:00'
WHERE id = 19;

-- ID 20: 2025-08-30 04:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'ABSENT',
    start_time = '2025-08-30 04:00:00',
    total_call_time = 0
WHERE id = 20;

-- 매칭 8 (matching_id: 8) - 04:00:00 시간대
-- ID 21: 2025-08-29 04:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-29 04:00:00',
    total_call_time = 1200  -- 20분
WHERE id = 21;

-- ID 22: 2025-08-30 04:00:00 (이미 COMPLETE 상태)
UPDATE volunteer_record SET 
    start_time = '2025-08-30 04:00:00'
WHERE id = 22;

-- 매칭 11-16 (matching_id: 11-16) - 15:00:00 시간대
-- ID 23: 2025-08-29 15:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-29 15:00:00',
    total_call_time = 1800  -- 30분
WHERE id = 23;

-- ID 24: 2025-08-29 15:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-29 15:00:00',
    total_call_time = 1200  -- 20분
WHERE id = 24;

-- ID 25: 2025-08-29 15:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'ABSENT',
    start_time = '2025-08-29 15:00:00',
    total_call_time = 0
WHERE id = 25;

-- ID 26: 2025-08-29 15:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'NOT_CONDUCTED',
    start_time = NULL,
    total_call_time = NULL
WHERE id = 26;

-- ID 27: 2025-08-29 15:00:00
UPDATE volunteer_record SET 
    volunteer_record_status = 'COMPLETE',
    start_time = '2025-08-29 15:00:00',
    total_call_time = 900   -- 15분
WHERE id = 27;

-- =====================================================
-- 2. Report 데이터 삽입 (완료된 기록들에만)
-- =====================================================

INSERT INTO report (health, mentality, opinion, volunteer_record_id, status, created_at, updated_at) VALUES
-- 매칭 6의 완료된 기록들
('GOOD', 'GOOD', '오늘따라 기분이 좋아 보이셨습니다. 식사도 잘 하셨고, 어제는 경로당에도 다녀오셨다고 합니다. 특별한 건강문제는 없어보입니다.', 1, 'Y', '2025-08-01 20:30:00', '2025-08-01 20:30:00'),
('NORMAL', 'GOOD', '오늘은 평소와 비슷한 컨디션이셨습니다. 식사는 잘 하셨지만 조금 피곤해 보이셨습니다. 내일은 더 좋아지실 것 같습니다.', 2, 'Y', '2025-08-05 20:25:00', '2025-08-05 20:25:00'),
('GOOD', 'NORMAL', '건강상태는 좋으시지만 오늘따라 조용하셨습니다. 무슨 일이 있으신지 걱정되지만 직접적으로 말씀하지는 않으셨습니다. 오래 통화해주셔서 감사했습니다.', 3, 'Y', '2025-08-08 20:35:00', '2025-08-08 20:35:00'),
('NORMAL', 'NORMAL', '오늘은 짧게 통화하셨지만 건강하시다고 하셨습니다. 바쁘신 것 같아서 일찍 끊어드렸습니다.', 5, 'Y', '2025-08-15 20:20:00', '2025-08-15 20:20:00'),
('GOOD', 'GOOD', '오늘은 정말 오래 통화하셨습니다. 가족 이야기부터 옛날 이야기까지 정말 재미있게 들었습니다. 건강하시고 기분도 좋으시다고 하셔서 다행입니다.', 7, 'Y', '2025-08-22 20:40:00', '2025-08-22 20:40:00'),
('NORMAL', 'NORMAL', '오늘은 짧게 통화하셨습니다. 바쁘신 것 같아서 일찍 끊어드렸습니다. 건강하시다고 하셨습니다.', 8, 'Y', '2025-08-26 20:15:00', '2025-08-26 20:15:00'),
('GOOD', 'GOOD', '오늘은 정말 오래 통화하셨습니다. 58분이나 통화하셔서 정말 즐거웠습니다. 건강하시고 기분도 좋으시다고 하셔서 다행입니다.', 9, 'Y', '2025-08-29 20:58:00', '2025-08-29 20:58:00'),

-- 매칭 5의 완료된 기록들
('GOOD', 'NORMAL', '오늘은 평소와 비슷하게 통화하셨습니다. 건강하시고 특별한 문제는 없으시다고 하셨습니다.', 10, 'Y', '2025-08-04 20:30:00', '2025-08-04 20:30:00'),
('NORMAL', 'GOOD', '오늘은 기분이 좋으셨습니다. 손자분이 방문하신다고 하셔서 기대에 찬 하루를 보내고 계셨습니다.', 11, 'Y', '2025-08-06 20:25:00', '2025-08-06 20:25:00'),
('GOOD', 'NORMAL', '오늘은 오래 통화하셨습니다. 건강상태는 좋으시지만 가족분들과 연락이 안 되셔서 걱정되신 것 같습니다. 내일은 연락이 될 것 같다고 위로드렸습니다.', 13, 'Y', '2025-08-13 20:35:00', '2025-08-13 20:35:00'),
('NORMAL', 'NORMAL', '오늘은 짧게 통화하셨습니다. 건강하시고 특별한 문제는 없으시다고 하셨습니다.', 14, 'Y', '2025-08-18 20:20:00', '2025-08-18 20:20:00'),
('GOOD', 'GOOD', '오늘은 기분이 좋으셨습니다. 식사도 잘 하시고 운동도 하셨다고 합니다. 건강하시고 활기차 보이셨습니다.', 16, 'Y', '2025-08-25 20:30:00', '2025-08-25 20:30:00'),
('NORMAL', 'GOOD', '오늘은 정말 오래 통화하셨습니다. 옛날 이야기부터 현재 이야기까지 정말 재미있게 들었습니다. 건강하시고 기분도 좋으시다고 하셔서 다행입니다.', 17, 'Y', '2025-08-27 20:40:00', '2025-08-27 20:40:00'),
('GOOD', 'GOOD', '새로운 9월이 시작되었습니다. 오늘은 아침부터 기분이 좋으셨다고 하셨습니다. 건강하시고 활기차 보이셨습니다.', 18, 'Y', '2025-09-01 20:25:00', '2025-09-01 20:25:00'),

-- 매칭 9의 완료된 기록
('NORMAL', 'NORMAL', '아침 봉사였습니다. 7분 정도 짧게 통화하셨지만 건강하시다고 하셨습니다. 아침 식사도 잘 하셨다고 합니다.', 19, 'Y', '2025-08-29 04:07:00', '2025-08-29 04:07:00'),

-- 매칭 8의 완료된 기록
('GOOD', 'NORMAL', '아침 봉사였습니다. 건강하시고 기분도 좋으시다고 하셨습니다. 오늘 하루도 건강하게 보내시라고 인사드렸습니다.', 21, 'Y', '2025-08-29 04:20:00', '2025-08-29 04:20:00'),
('NORMAL', 'GOOD', '아침 봉사였습니다. 건강하시고 기분도 좋으시다고 하셨습니다. 오늘 하루도 건강하게 보내시라고 인사드렸습니다.', 22, 'Y', '2025-08-30 04:15:00', '2025-08-30 04:15:00'),

-- 매칭 11-16의 완료된 기록들
('NORMAL', 'GOOD', '오후 봉사였습니다. 점심 식사도 잘 하시고 오후에 휴식을 취하신다고 하셨습니다. 건강하시고 기분도 좋으시다고 하셔서 다행입니다.', 23, 'Y', '2025-08-29 15:30:00', '2025-08-29 15:30:00'),
('GOOD', 'NORMAL', '오후 봉사였습니다. 건강하시고 특별한 문제는 없으시다고 하셨습니다.', 24, 'Y', '2025-08-29 15:20:00', '2025-08-29 15:20:00'),
('NORMAL', 'NORMAL', '오후 봉사였습니다. 짧게 통화하셨지만 건강하시다고 하셨습니다.', 27, 'Y', '2025-08-29 15:15:00', '2025-08-29 15:15:00');

-- =====================================================
-- 3. CallHistory 데이터 삽입
-- =====================================================

-- 매칭 6의 통화 이력
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-08-01 20:00:00', 1800, 1, 'Y', '2025-08-01 20:00:00', '2025-08-01 20:00:00'),   -- 30분
('2025-08-05 20:00:00', 1500, 2, 'Y', '2025-08-05 20:00:00', '2025-08-05 20:00:00'),   -- 25분
('2025-08-08 20:00:00', 2100, 3, 'Y', '2025-08-08 20:00:00', '2025-08-08 20:00:00'),   -- 35분
('2025-08-12 20:00:00', 0, 4, 'Y', '2025-08-12 20:00:00', '2025-08-12 20:00:00'),      -- 부재중
('2025-08-15 20:00:00', 1200, 5, 'Y', '2025-08-15 20:00:00', '2025-08-15 20:00:00'),   -- 20분
('2025-08-22 20:00:00', 2400, 7, 'Y', '2025-08-22 20:00:00', '2025-08-22 20:00:00'),   -- 40분
('2025-08-26 20:00:00', 900, 8, 'Y', '2025-08-26 20:00:00', '2025-08-26 20:00:00'),    -- 15분
('2025-08-29 20:00:00', 3492, 9, 'Y', '2025-08-29 20:00:00', '2025-08-29 20:00:00');   -- 58분 12초

-- 매칭 5의 통화 이력
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-08-04 20:00:00', 1800, 10, 'Y', '2025-08-04 20:00:00', '2025-08-04 20:00:00'),  -- 30분
('2025-08-06 20:00:00', 1500, 11, 'Y', '2025-08-06 20:00:00', '2025-08-06 20:00:00'),  -- 25분
('2025-08-11 20:00:00', 0, 12, 'Y', '2025-08-11 20:00:00', '2025-08-11 20:00:00'),     -- 부재중
('2025-08-13 20:00:00', 2100, 13, 'Y', '2025-08-13 20:00:00', '2025-08-13 20:00:00'),  -- 35분
('2025-08-18 20:00:00', 1200, 14, 'Y', '2025-08-18 20:00:00', '2025-08-18 20:00:00'),  -- 20분
('2025-08-25 20:00:00', 1800, 16, 'Y', '2025-08-25 20:00:00', '2025-08-25 20:00:00'),  -- 30분
('2025-08-27 20:00:00', 2400, 17, 'Y', '2025-08-27 20:00:00', '2025-08-27 20:00:00'),  -- 40분
('2025-09-01 20:00:00', 1500, 18, 'Y', '2025-09-01 20:00:00', '2025-09-01 20:00:00');  -- 25분

-- 매칭 9의 통화 이력
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-08-29 04:00:00', 432, 19, 'Y', '2025-08-29 04:00:00', '2025-08-29 04:00:00'),   -- 7분 12초
('2025-08-30 04:00:00', 0, 20, 'Y', '2025-08-30 04:00:00', '2025-08-30 04:00:00');     -- 부재중

-- 매칭 8의 통화 이력
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-08-29 04:00:00', 1200, 21, 'Y', '2025-08-29 04:00:00', '2025-08-29 04:00:00'),  -- 20분
('2025-08-30 04:00:00', 0, 22, 'Y', '2025-08-30 04:00:00', '2025-08-30 04:00:00');     -- 부재중

-- 매칭 11-16의 통화 이력
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-08-29 15:00:00', 1800, 23, 'Y', '2025-08-29 15:00:00', '2025-08-29 15:00:00'),  -- 30분
('2025-08-29 15:00:00', 1200, 24, 'Y', '2025-08-29 15:00:00', '2025-08-29 15:00:00'),  -- 20분
('2025-08-29 15:00:00', 0, 25, 'Y', '2025-08-29 15:00:00', '2025-08-29 15:00:00'),     -- 부재중
('2025-08-29 15:00:00', 900, 27, 'Y', '2025-08-29 15:00:00', '2025-08-29 15:00:00');   -- 15분

-- =====================================================
-- 4. 데이터 확인 쿼리
-- =====================================================

-- 상태별 봉사 기록 수 확인
SELECT 
    volunteer_record_status,
    COUNT(*) as count
FROM volunteer_record 
GROUP BY volunteer_record_status
ORDER BY count DESC;

-- 매칭별 봉사 기록 통계
SELECT 
    vr.matching_id,
    COUNT(*) as total_records,
    SUM(CASE WHEN vr.volunteer_record_status = 'COMPLETE' THEN 1 ELSE 0 END) as completed_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'ABSENT' THEN 1 ELSE 0 END) as absent_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'NOT_CONDUCTED' THEN 1 ELSE 0 END) as not_conducted_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'PENDING' THEN 1 ELSE 0 END) as pending_count
FROM volunteer_record vr
GROUP BY vr.matching_id
ORDER BY vr.matching_id;

-- 시간대별 봉사 기록 통계
SELECT 
    vr.scheduled_time,
    COUNT(*) as total_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'COMPLETE' THEN 1 ELSE 0 END) as completed_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'ABSENT' THEN 1 ELSE 0 END) as absent_count,
    SUM(CASE WHEN vr.volunteer_record_status = 'NOT_CONDUCTED' THEN 1 ELSE 0 END) as not_conducted_count
FROM volunteer_record vr
GROUP BY vr.scheduled_time
ORDER BY vr.scheduled_time;

-- 완료된 봉사 기록과 리포트 조회
SELECT 
    vr.id,
    vr.matching_id,
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
