-- =====================================================
-- 간단한 VolunteerRecord 더미 데이터
-- =====================================================

-- 1. 기존 PENDING 레코드들을 다양한 상태로 변경
UPDATE volunteer_record SET volunteer_record_status = 'COMPLETE', start_time = '2025-01-20 14:00:00', total_call_time = 1800 WHERE id = 1;
UPDATE volunteer_record SET volunteer_record_status = 'COMPLETE', start_time = '2025-01-22 15:30:00', total_call_time = 1200 WHERE id = 2;
UPDATE volunteer_record SET volunteer_record_status = 'COMPLETE', start_time = '2025-01-24 16:45:00', total_call_time = 2100 WHERE id = 3;
UPDATE volunteer_record SET volunteer_record_status = 'ABSENT', start_time = '2025-01-21 14:00:00', total_call_time = 0 WHERE id = 4;
UPDATE volunteer_record SET volunteer_record_status = 'NOT_CONDUCTED', start_time = NULL, total_call_time = NULL WHERE id = 5;

-- 2. Report 데이터 추가 (완료된 기록들에만)
INSERT INTO report (health, mentality, opinion, volunteer_record_id, status, created_at, updated_at) VALUES
('GOOD', 'GOOD', '오늘따라 기분이 좋아 보이셨습니다. 식사도 잘 하셨고, 어제는 경로당에도 다녀오셨다고 합니다.', 1, 'Y', NOW(), NOW()),
('NORMAL', 'GOOD', '오늘은 평소와 비슷한 컨디션이셨습니다. 식사는 잘 하셨지만 조금 피곤해 보이셨습니다.', 2, 'Y', NOW(), NOW()),
('GOOD', 'NORMAL', '건강상태는 좋으시지만 오늘따라 조용하셨습니다. 무슨 일이 있으신지 걱정되지만 직접적으로 말씀하지는 않으셨습니다.', 3, 'Y', NOW(), NOW());

-- 3. CallHistory 데이터 추가
INSERT INTO call_history (start_time, call_time, volunteer_record_id, status, created_at, updated_at) VALUES
('2025-01-20 14:00:00', 1800, 1, 'Y', NOW(), NOW()),
('2025-01-22 15:30:00', 1200, 2, 'Y', NOW(), NOW()),
('2025-01-24 16:45:00', 2100, 3, 'Y', NOW(), NOW()),
('2025-01-21 14:00:00', 0, 4, 'Y', NOW(), NOW());

-- =====================================================
-- 데이터 확인 쿼리
-- =====================================================

-- 상태별 봉사 기록 수 확인
SELECT 
    volunteer_record_status,
    COUNT(*) as count
FROM volunteer_record 
GROUP BY volunteer_record_status;

-- 완료된 봉사 기록과 리포트 조회
SELECT 
    vr.id,
    vr.volunteer_record_status,
    vr.scheduled_date,
    vr.start_time,
    vr.total_call_time,
    r.health,
    r.mentality,
    r.opinion
FROM volunteer_record vr
LEFT JOIN report r ON vr.id = r.volunteer_record_id
WHERE vr.volunteer_record_status = 'COMPLETE'
ORDER BY vr.scheduled_date DESC;
