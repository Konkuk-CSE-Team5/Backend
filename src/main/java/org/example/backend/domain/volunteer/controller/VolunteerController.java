package org.example.backend.domain.volunteer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.volunteer.dto.GetVolunteerMeResponse;
import org.example.backend.domain.volunteer.dto.PatchVolunteerMeRequest;
import org.example.backend.domain.volunteer.dto.RegisterVolunteerRequest;
import org.example.backend.domain.volunteer.service.RegisterVolunteerService;
import org.example.backend.domain.volunteer.service.VolunteerMeService;
import org.example.backend.global.auth.annotation.LoginUserId;
import org.example.backend.global.common.response.BaseResponse;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.springframework.web.bind.annotation.*;

import static org.example.backend.global.swagger.SwaggerResponseDescription.DEFAULT;
import static org.example.backend.global.swagger.SwaggerResponseDescription.REGISTER;

@Tag(
        name = "Volunteer",
        description = "봉사자 관련 API"
)
@Slf4j
@RequiredArgsConstructor
@RequestMapping("volunteers")
@RestController
public class VolunteerController {
    private final RegisterVolunteerService registerVolunteerService;
    private final VolunteerMeService volunteerMeService;

    @Operation(
            summary = "봉사자 회원가입 API",
            description = "봉사자의 회원가입을 수행하는 API"
    )
    @CustomExceptionDescription(REGISTER)
    @PostMapping
    public BaseResponse<Void> registerVolunteer(@RequestBody RegisterVolunteerRequest request) {
        registerVolunteerService.register(request);
        return new BaseResponse<>(null);
    }

    @Operation(
            summary = "봉사자 설정화면 조회",
            description = "봉사자의 설정화면 정보를 조회하는 API"
    )
    @CustomExceptionDescription(DEFAULT)
    @GetMapping("me")
    public BaseResponse<GetVolunteerMeResponse> getVolunteerMe(@LoginUserId @Parameter(hidden = true) Long loginUserId) {
        return new BaseResponse<>(volunteerMeService.get(loginUserId));
    }

    @Operation(
            summary = "봉사자 설정화면 조회",
            description = "봉사자의 설정화면 정보를 조회하는 API"
    )
    @CustomExceptionDescription(DEFAULT)
    @PatchMapping("me")
    public BaseResponse<Void> patchVolunteerMe(
            @LoginUserId @Parameter(hidden = true) Long loginUserId,
            @RequestBody @jakarta.validation.Valid PatchVolunteerMeRequest request) {
        volunteerMeService.patch(loginUserId, request);
        return new BaseResponse<>(null);
    }


}
