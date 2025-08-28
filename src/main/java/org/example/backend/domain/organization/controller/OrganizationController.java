package org.example.backend.domain.organization.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.organization.dto.RegisterOrganizationRequest;
import org.example.backend.domain.organization.service.RegisterOrganizationService;
import org.example.backend.domain.volunteer.dto.RegisterVolunteerRequest;
import org.example.backend.global.common.response.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("organizations")
@RestController
public class OrganizationController {
    private final RegisterOrganizationService registerOrganizationService;
    @PostMapping
    public BaseResponse<Void> register(@RequestBody RegisterOrganizationRequest request){
        registerOrganizationService.register(request);
        return new BaseResponse<>(null);
    }
}
