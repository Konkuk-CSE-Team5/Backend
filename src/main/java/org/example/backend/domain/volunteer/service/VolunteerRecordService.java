package org.example.backend.domain.volunteer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.matching.repository.MatchingRepository;
import org.example.backend.domain.record.model.CallHistory;
import org.example.backend.domain.record.model.HealthLevel;
import org.example.backend.domain.record.model.MentalityLevel;
import org.example.backend.domain.record.model.Report;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.repository.CallHistoryRepository;
import org.example.backend.domain.record.repository.ReportRepository;
import org.example.backend.domain.record.repository.VolunteerRecordRepository;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.senior.repository.SeniorRepository;

import org.example.backend.domain.volunteer.dto.GetVolunteerRecordDetailResponse;
import org.example.backend.domain.volunteer.dto.GetVolunteerRecordResponse;
import org.example.backend.domain.volunteer.dto.GetVolunteerRecordUpdateFormResponse;

import org.example.backend.domain.volunteer.dto.PatchVolunteerRecordRequest;
import org.example.backend.domain.volunteer.dto.PostVolunteerRecordRequest;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.domain.volunteer.repository.VolunteerRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;

@Slf4j
@RequiredArgsConstructor
@Service
public class VolunteerRecordService {
    private final VolunteerRepository volunteerRepository;
    private final SeniorRepository seniorRepository;
    private final MatchingRepository matchingRepository;
    private final VolunteerRecordRepository volunteerRecordRepository;
    private final ReportRepository reportRepository;
    private final CallHistoryRepository callHistoryRepository;

    @Transactional
    public void setRecord(Long loginUserId, PostVolunteerRecordRequest request) {
        // 봉사자 정보 조회
        Volunteer volunteer = volunteerRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));
        // 어르신 정보 조회
        Senior senior = seniorRepository.findById(request.seniorId())
                .orElseThrow(() -> new CustomException(BAD_REQUEST));
        // 매칭 정보 조회 (활성 상태)
        Matching matching = matchingRepository.findByVolunteerIdAndSeniorIdAndMatchingStatus(
                        volunteer.getId(), senior.getId(), MatchingStatus.ACTIVE)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));
        // record 조회
        LocalDate today = LocalDate.now();
        VolunteerRecord volunteerRecord = volunteerRecordRepository.findByMatchingAndScheduledDate(matching, today)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));
        
        // 요청의 상태를 VolunteerRecord에 적용
        volunteerRecord.updateStatus(request.status());
        
        // report 생성 또는 업데이트 (upsert)
        Report report = reportRepository.findByVolunteerRecord(volunteerRecord)
                .map(existingReport -> {
                    // 기존 리포트가 있으면 업데이트
                    existingReport.updateReport(request.health(), request.mentality(), request.opinion());
                    return existingReport;
                })
                .orElseGet(() -> {
                    // 기존 리포트가 없으면 새로 생성
                    return Report.builder()
                            .health(request.health())
                            .mentality(request.mentality())
                            .opinion(request.opinion())
                            .volunteerRecord(volunteerRecord)
                            .build();
                });
        reportRepository.save(report);

        // 기존 call history 삭제 (중복 방지)
        callHistoryRepository.deleteByVolunteerRecord(volunteerRecord);

        // 새로운 call history 생성
        List<CallHistory> callHistories = request.callHistory().stream()
                .map(ch -> CallHistory.builder()
                        .startTime(ch.dateTime())
                        .callTime(ch.callTime())
                        .volunteerRecord(volunteerRecord)
                        .build())
                .toList();
        callHistoryRepository.saveAll(callHistories);

        // totalCallTime 계산 및 업데이트
        Duration totalCallTime = request.callHistory().stream()
                .map(ch -> ch.callTime())
                .reduce(Duration.ZERO, Duration::plus);
        volunteerRecord.updateTotalCallTime(totalCallTime);
        volunteerRecordRepository.save(volunteerRecord);
    }

    public GetVolunteerRecordResponse getRecords(Long loginUserId) {
        // 봉사자 정보 조회
        Volunteer volunteer = volunteerRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));
        List<Matching> matchingList = matchingRepository.findAllByVolunteer(volunteer);
        if (matchingList.isEmpty()) {
            return GetVolunteerRecordResponse.builder()
                    .seniors(List.of())
                    .build();
        }
        List<GetVolunteerRecordResponse.SeniorDto> seniorDtos = matchingList.stream()
                .map(m -> {
                    // 어르신 정보
                    Senior senior = m.getSenior();
                    // 봉사 기록
                    List<VolunteerRecord> records = volunteerRecordRepository.findAllByMatchingOrderByIdDesc(m);
                    // 통화 요약
                    int totalCalls = records.size();
                    var totalDuration = records.stream()
                            .map(r -> r.getTotalCallTime() == null ? 0 : r.getTotalCallTime().toSeconds())
                            .reduce(0L, Long::sum);
                    return GetVolunteerRecordResponse.SeniorDto.builder()
                            .seniorId(senior.getId())
                            .seniorName(senior.getName())
                            .status(m.getMatchingStatus())
                            .summary(GetVolunteerRecordResponse.Summary.builder()
                                    .totalCalls(totalCalls)
                                    .totalDuration(java.time.Duration.ofSeconds(totalDuration))
                                    .build())
                            .records(records.stream()
                                    .map(r -> GetVolunteerRecordResponse.Record.builder()
                                            .recordId(r.getId())
                                            .dateTime(r.getStartTime())
                                            .duration(r.getTotalCallTime())
                                            .status(r.getVolunteerRecordStatus())
                                            .build()
                                    )
                                    .toList())
                            .build();
                })
                .toList();

        return GetVolunteerRecordResponse.builder()
                .seniors(seniorDtos)
                .build();
    }

    public GetVolunteerRecordDetailResponse getRecord(Long loginUserId, Long recordId) {
        // 봉사자 정보 확인
        Volunteer volunteer = volunteerRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));

        // 기록 조회
        VolunteerRecord record = volunteerRecordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));

        // 권한 체크: 기록의 매칭 봉사자와 로그인 사용자 일치 여부
        if (!Objects.equals(record.getMatching().getVolunteer().getId(), volunteer.getId())) {
            throw new CustomException(BAD_REQUEST);
        }

        // 해당 매칭의 모든 기록 조회
        Matching matching = record.getMatching();
        Senior senior = matching.getSenior();
        List<VolunteerRecord> allRecords = volunteerRecordRepository.findAllByMatchingOrderByIdDesc(matching);

        return GetVolunteerRecordDetailResponse.builder()
                .seniorId(senior.getId())
                .seniorName(senior.getName())
                .records(allRecords.stream()
                        .map(r -> GetVolunteerRecordDetailResponse.Record.builder()
                                .recordId(r.getId())
                                .dateTime(r.getStartTime())
                                .duration(r.getTotalCallTime())
                                .status(r.getVolunteerRecordStatus())
                                .build())
                        .toList())
                .build();
    }

    public GetVolunteerRecordUpdateFormResponse getRecordUpdateForm(Long loginUserId, Long recordId) {
        // 봉사자 정보 확인
        Volunteer volunteer = volunteerRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));

        // 기록 조회
        VolunteerRecord record = volunteerRecordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));

        // 권한 체크: 기록의 매칭 봉사자와 로그인 사용자 일치 여부
        if (!Objects.equals(record.getMatching().getVolunteer().getId(), volunteer.getId())) {
            throw new CustomException(BAD_REQUEST);
        }

        // 리포트 및 콜히스토리 조회
        Report report = record.getReport();
        List<CallHistory> callHistories = callHistoryRepository.findAllByVolunteerRecordOrderByStartTimeAsc(record);

        return GetVolunteerRecordUpdateFormResponse.builder()
                .callHistory(callHistories.stream()
                        .map(ch -> GetVolunteerRecordUpdateFormResponse.CallHistoryDto.builder()
                                .dateTime(ch.getStartTime())
                                .callTime(ch.getCallTime())
                                .build())
                        .toList())
                .status(record.getVolunteerRecordStatus())
                .health(report == null ? null : report.getHealth())
                .mentality(report == null ? null : report.getMentality())
                .opinion(report == null ? null : report.getOpinion())
                .build();
    }

    @Transactional
    public void updateRecord(Long loginUserId, Long recordId, PatchVolunteerRecordRequest request) {
        // 봉사자 정보 확인
        Volunteer volunteer = volunteerRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));

        // 기록 조회
        VolunteerRecord record = volunteerRecordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));

        // 권한 체크: 기록의 매칭 봉사자와 로그인 사용자 일치 여부
        if (!Objects.equals(record.getMatching().getVolunteer().getId(), volunteer.getId())) {
            throw new CustomException(BAD_REQUEST);
        }

        // 기록 상태 업데이트 (null이 아닌 경우에만)
        if (request.status() != null) {
            record.updateStatus(request.status());
        }

        // 리포트 관련 필드가 하나라도 있는 경우에만 리포트 처리
        if (request.health() != null || request.mentality() != null || request.opinion() != null) {
            Report report = reportRepository.findByVolunteerRecord(record)
                    .map(existingReport -> {
                        // 기존 리포트가 있으면 null이 아닌 값들만 업데이트
                        HealthLevel newHealth = request.health() != null ? request.health() : existingReport.getHealth();
                        MentalityLevel newMentality = request.mentality() != null ? request.mentality() : existingReport.getMentality();
                        String newOpinion = request.opinion() != null ? request.opinion() : existingReport.getOpinion();
                        
                        existingReport.updateReport(newHealth, newMentality, newOpinion);
                        return existingReport;
                    })
                    .orElseGet(() -> {
                        // 기존 리포트가 없으면 새로 생성 (null 값들은 기본값 또는 null로 처리)
                        return Report.builder()
                                .health(request.health())
                                .mentality(request.mentality())
                                .opinion(request.opinion())
                                .volunteerRecord(record)
                                .build();
                    });
            reportRepository.save(report);
        }

        // VolunteerRecord 변경사항 저장
        volunteerRecordRepository.save(record);
    }
}
