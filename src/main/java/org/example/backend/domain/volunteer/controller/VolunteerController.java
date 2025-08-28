package org.example.backend.domain.volunteer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.volunteer.dto.RegisterVolunteerRequest;
import org.example.backend.domain.volunteer.service.RegisterVolunteerService;
import org.example.backend.global.common.response.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("volunteers")
@RestController
public class VolunteerController {
    private final RegisterVolunteerService registerVolunteerService;

    @PostMapping
    public BaseResponse<Void> registerVolunteer(@RequestBody RegisterVolunteerRequest request){
        registerVolunteerService.register(request);
        return new BaseResponse<>(null);
    }
}
