package org.example.backend.domain.record.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.record.dto.GetRecordResponse;
import org.example.backend.domain.record.service.RetrieveVolunteerRecordService;
import org.example.backend.global.common.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("records")
@RestController
public class RecordController {
    private final RetrieveVolunteerRecordService retrieveVolunteerRecordService;
    @GetMapping("/{recordId}")
    public BaseResponse<GetRecordResponse> retrieveVolunteerRecord(@PathVariable Long recordId){
        return new BaseResponse<>(retrieveVolunteerRecordService.retrieve(recordId));
    }
}
