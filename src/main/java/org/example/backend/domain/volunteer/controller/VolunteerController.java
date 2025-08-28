package org.example.backend.domain.volunteer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.volunteer.dto.RegisterVolunteerRequest;
import org.example.backend.domain.volunteer.service.RegisterVolunteerService;
import org.example.backend.global.common.response.BaseResponse;
import org.example.backend.global.swagger.CustomExceptionDescription;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(
            summary = "봉사자 회원가입 API",
            description = "봉사자의 회원가입을 수행하는 API"
    )
    @CustomExceptionDescription(REGISTER)
    @PostMapping
    public BaseResponse<Void> registerVolunteer(@RequestBody RegisterVolunteerRequest request){
        registerVolunteerService.register(request);
        return new BaseResponse<>(null);
    }
}
