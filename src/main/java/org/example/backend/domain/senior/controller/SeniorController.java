package org.example.backend.domain.senior.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.senior.dto.GetSeniorMatchingRecords;
import org.example.backend.domain.senior.service.SeniorService;
import org.example.backend.global.auth.annotation.LoginUserId;
import org.example.backend.global.common.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("seniors")
@RestController
public class SeniorController {
    private final SeniorService seniorService;
    @GetMapping("/{seniorId}/records")
    public BaseResponse<GetSeniorMatchingRecords> getCurrentMatchingRecords(@LoginUserId Long userId, @PathVariable Long seniorId){
        return new BaseResponse<>(seniorService.getCurrentMatchingRecords(userId, seniorId));
    }
}
