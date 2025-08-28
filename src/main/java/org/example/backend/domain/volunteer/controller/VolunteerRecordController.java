package org.example.backend.domain.volunteer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.volunteer.dto.*;

import org.example.backend.domain.volunteer.service.VolunteerRecordService;
import org.example.backend.global.auth.annotation.LoginUserId;
import org.example.backend.global.common.response.BaseResponse;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.springframework.web.bind.annotation.*;

import static org.example.backend.global.swagger.SwaggerResponseDescription.DEFAULT;


@Slf4j
@RestController
@RequestMapping("volunteers/records")
@RequiredArgsConstructor
@Tag(
        name = "Volunteer Records",
        description = "봉사 기록 관련 API"
)
public class VolunteerRecordController {

    private final VolunteerRecordService volunteerRecordService;

    @GetMapping
    public BaseResponse<GetVolunteerRecordResponse> getRecords(@LoginUserId @Parameter(hidden = true) Long loginUserId) {
        return new BaseResponse<>(volunteerRecordService.getRecords(loginUserId));
    }
    @PatchMapping
    public BaseResponse<Void> setRecord(
            @LoginUserId @Parameter(hidden = true) Long loginUserId,
            @RequestBody @Valid PostVolunteerRecordRequest request
    ) {
        volunteerRecordService.setRecord(loginUserId, request);
        return new BaseResponse<>(null);
    }

    @Operation(
            summary = "봉사 기록 상세 조회",
            description = "특정 봉사 기록의 상세 정보를 조회하는 API"
    )
    @CustomExceptionDescription(DEFAULT)
    @GetMapping("{recordId}")
    public BaseResponse<GetVolunteerRecordDetailResponse> getRecord(
            @LoginUserId @Parameter(hidden = true) Long loginUserId,
            @PathVariable Long recordId) {
        return new BaseResponse<>(volunteerRecordService.getRecord(loginUserId, recordId));
    }

    @Operation(
            summary = "봉사 기록 수정 폼 조회",
            description = "특정 봉사 기록의 수정 폼에 표시할 정보를 조회하는 API"
    )
    @CustomExceptionDescription(DEFAULT)
    @GetMapping("{recordId}/updateForm")
    public BaseResponse<GetVolunteerRecordUpdateFormResponse> getRecordUpdateForm(
            @LoginUserId @Parameter(hidden = true) Long loginUserId,
            @PathVariable Long recordId) {
        return new BaseResponse<>(volunteerRecordService.getRecordUpdateForm(loginUserId, recordId));
    }
//
//    @PatchMapping("{recordId}")
//    public BaseResponse<Void> updateRecord(
//            @LoginUserId Long loginUserId,
//            @PathVariable Long recordId,
//            @RequestBody @Valid PatchVolunteerRecordRequest request) {
//        volunteerRecordService.updateRecord(loginUserId, recordId, request);
//        return new BaseResponse<>(null);
//    }
}