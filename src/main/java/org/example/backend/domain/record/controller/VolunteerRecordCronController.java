package org.example.backend.domain.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.record.service.VolunteerRecordCronService;
import org.example.backend.global.common.response.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "VolunteerRecord Cron", description = "봉사 기록 Cron Job 관련 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/volunteer-records/cron")
@RestController
public class VolunteerRecordCronController {

    private final VolunteerRecordCronService volunteerRecordCronService;

    @Operation(
            summary = "만료된 PENDING 봉사 기록 수동 업데이트",
            description = "테스트용으로 만료된 PENDING 상태의 봉사 기록을 NOT_CONDUCTED로 수동 업데이트하는 API"
    )
    @PostMapping("/update-expired")
    public BaseResponse<String> manuallyUpdateExpiredPendingRecords() {
        log.info("[Manual API] 만료된 PENDING 봉사 기록 수동 업데이트 요청");
        
        int updatedCount = volunteerRecordCronService.manuallyUpdateExpiredPendingRecords();
        
        String message = String.format("만료된 PENDING 봉사 기록 업데이트 완료 - 총 %d개 레코드가 NOT_CONDUCTED로 변경됨", updatedCount);
        
        return new BaseResponse<>(message);
    }
}
