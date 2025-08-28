package org.example.backend.domain.record.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.record.dto.GetRecordResponse;
import org.example.backend.domain.record.service.RetrieveVolunteerRecordService;
import org.example.backend.global.common.response.BaseResponse;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.backend.global.swagger.SwaggerResponseDescription.MAIN;

@Tag(
        name = "Record",
        description = "기록 관련 API"
)
@Slf4j
@RequiredArgsConstructor
@RequestMapping("records")
@RestController
public class RecordController {
    private final RetrieveVolunteerRecordService retrieveVolunteerRecordService;

    @Operation(
            summary = "상세 기록 조회 API",
            description = "상세 기록 조회를 수행하는 API"
    )
    @CustomExceptionDescription(MAIN)
    @GetMapping("/{recordId}")
    public BaseResponse<GetRecordResponse> retrieveVolunteerRecord(@Parameter @PathVariable Long recordId){
        return new BaseResponse<>(retrieveVolunteerRecordService.retrieve(recordId));
    }
}
