package org.example.backend.domain.organization.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.matching.repository.MatchingRepository;
import org.example.backend.domain.organization.dto.GetOrgSeniorsResponse;
import org.example.backend.domain.organization.dto.GetSeniorManagePageResponse;
import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.organization.repository.OrganizationRepository;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.example.backend.domain.record.repository.VolunteerRecordRepository;
import org.example.backend.domain.schedule.model.Schedule;
import org.example.backend.domain.schedule.repository.ScheduleRepository;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.senior.repository.SeniorRepository;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.global.common.exception.CustomException;
import org.example.backend.global.util.LocalDateTimeUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.ENTITY_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetOrgSeniorsService {
    private final OrganizationRepository organizationRepository;
    private final SeniorRepository seniorRepository;
    private final MatchingRepository matchingRepository;
    private final ScheduleRepository scheduleRepository;
    private final VolunteerRecordRepository volunteerRecordRepository;

    public GetOrgSeniorsResponse getSeniors(Long orgUserId) {
        log.info("orgUserId = {}", orgUserId);

        // 기관 조회
        Organization organization = organizationRepository.findByUserId(orgUserId).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));

        // 시니어들을 조회
        List<Senior> seniors = seniorRepository.findAllByOrganization(organization);

        // 응답 생성
        List<GetOrgSeniorsResponse.SeniorDto> collect = seniors.stream().map(senior -> {
            Matching lastMatching = matchingRepository.findTopBySeniorOrderByIdDesc(senior).get();
            GetOrgSeniorsResponse.SeniorDto seniorDto = GetOrgSeniorsResponse.SeniorDto.entityToDto(senior, lastMatching.getMatchingStatus());
            return seniorDto;
        }).collect(Collectors.toList());

        return new GetOrgSeniorsResponse(collect);
    }


    public GetSeniorManagePageResponse getSenior(Long orgUserId, Long seniorId) {
        log.info("orgUserId = {} seniorId = {}", orgUserId, seniorId);

        // 기관 조회
        Organization organization = organizationRepository.findByUserId(orgUserId).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));

        // 어르신을 찾고
        Senior senior = seniorRepository.findById(seniorId).orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
        if(!senior.getOrganization().equals(organization)){
            throw new CustomException(ENTITY_NOT_FOUND);
        }

        // 매칭을 찾고
        Matching matching = matchingRepository.findTopBySeniorIdAndMatchingStatus(senior.getId(), MatchingStatus.ACTIVE).get();

        // 봉사 기간 조회
        Schedule schedule = scheduleRepository.findByMatching(matching).get();

        // 응답
        GetSeniorManagePageResponse.SeniorProfile seniorProfile = new GetSeniorManagePageResponse.SeniorProfile(senior.getName(),
                LocalDateTimeUtil.toFormattedString(senior.getBirthday()),
                senior.getPhone(),
                LocalDateTimeUtil.toFormattedString(schedule.getStartDate()),
                LocalDateTimeUtil.toFormattedString(schedule.getEndDate())
        );

        // 봉사자도 찾고
        GetSeniorManagePageResponse.MatchedVolunteer matchedVolunteer = null;
        Volunteer volunteer = matching.getVolunteer();
        if(volunteer != null){
            Optional<VolunteerRecord> optRecord = volunteerRecordRepository.findTopByMatchingOrderByIdDesc(matching);
            String lastWorkDate = optRecord.map(record -> LocalDateTimeUtil.toFormattedString(record.getScheduledDate())).orElse(null);
            matchedVolunteer =  new GetSeniorManagePageResponse.MatchedVolunteer(volunteer.getName(),lastWorkDate );
        }

        return new GetSeniorManagePageResponse(seniorProfile, senior.getCode(), matchedVolunteer);
    }
}
