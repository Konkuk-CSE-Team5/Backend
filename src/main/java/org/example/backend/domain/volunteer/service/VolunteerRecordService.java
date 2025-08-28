package org.example.backend.domain.volunteer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.matching.MatchingRepository;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.record.model.CallHistory;
import org.example.backend.domain.record.model.Report;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.repository.CallHistoryRepository;
import org.example.backend.domain.record.repository.ReportRepository;
import org.example.backend.domain.record.repository.VolunteerRecordRepository;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.senior.repository.SeniorRepository;
import org.example.backend.domain.users.model.User;
import org.example.backend.domain.users.repository.UserRepository;
import org.example.backend.domain.volunteer.dto.GetVolunteerMeResponse;
import org.example.backend.domain.volunteer.dto.GetVolunteerRecordResponse;
import org.example.backend.domain.volunteer.dto.PatchVolunteerMeRequest;
import org.example.backend.domain.volunteer.dto.PostVolunteerRecordRequest;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.domain.volunteer.repository.VolunteerRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
    public void createRecord(Long loginUserId, PostVolunteerRecordRequest request) {
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
        // report 생성
        Report report = Report.builder()
                .health(request.health())
                .mentality(request.mentality())
                .opinion(request.opinion())
                .volunteerRecord(volunteerRecord)
                .build();
        reportRepository.save(report);

        // call history 생성
        List<CallHistory> callHistories = request.callHistory().stream()
                .map(ch -> CallHistory.builder()
                        .startTime(ch.dateTime())
                        .callTime(ch.callTime())
                        .volunteerRecord(volunteerRecord)
                        .build())
                .toList();
        callHistoryRepository.saveAll(callHistories);
    }

    public GetVolunteerRecordResponse getRecords(Long loginUserId) {
        // 봉사자 정보 조회
        Volunteer volunteer = volunteerRepository.findByUserId(loginUserId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST));
        List<Matching> matchingList = matchingRepository.findAllByVolunteer(volunteer);
        if (matchingList.isEmpty()) return null;
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
}
