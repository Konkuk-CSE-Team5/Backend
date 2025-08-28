package org.example.backend.domain.volunteer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.volunteer.dto.GetVolunteerMeResponse;
import org.example.backend.domain.volunteer.dto.PatchVolunteerMeRequest;
import org.example.backend.domain.volunteer.dto.PostVolunteerRecordRequest;
import org.example.backend.domain.volunteer.dto.RegisterVolunteerRequest;
import org.example.backend.domain.volunteer.service.RegisterVolunteerService;
import org.example.backend.domain.volunteer.service.VolunteerMeService;
import org.example.backend.domain.volunteer.service.VolunteerRecordService;
import org.example.backend.global.auth.annotation.LoginUserId;
import org.example.backend.global.common.response.BaseResponse;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.springframework.web.bind.annotation.*;

import static org.example.backend.global.swagger.SwaggerResponseDescription.DEFAULT;
import static org.example.backend.global.swagger.SwaggerResponseDescription.REGISTER;


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
    public BaseResponse<List<GetVolunteerRecordResponse>> getRecords(@LoginUserId @Parameter(hidden = true) Long loginUserId) {
        return new BaseResponse<>(volunteerRecordService.getRecords(loginUserId));
    }
    @PatchMapping
    public BaseResponse<Void> createRecord(
            @LoginUserId Long loginUserId,
            @RequestBody @Valid PostVolunteerRecordRequest request
    ) {
        volunteerRecordService.createRecord(loginUserId, request);
        return new BaseResponse<>(null);
    }

    @GetMapping("{recordId}")
    public BaseResponse<GetVolunteerRecordDetailResponse> getRecord(
            @LoginUserId Long loginUserId,
            @PathVariable Long recordId) {
        return new BaseResponse<>(volunteerRecordService.getRecord(loginUserId, recordId));
    }

    @PatchMapping("{recordId}")
    public BaseResponse<Void> updateRecord(
            @LoginUserId Long loginUserId,
            @PathVariable Long recordId,
            @RequestBody @Valid PatchVolunteerRecordRequest request) {
        volunteerRecordService.updateRecord(loginUserId, recordId, request);
        return new BaseResponse<>(null);
    }
}