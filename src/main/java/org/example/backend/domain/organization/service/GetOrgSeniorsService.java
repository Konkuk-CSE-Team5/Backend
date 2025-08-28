package org.example.backend.domain.organization.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.repository.MatchingRepository;
import org.example.backend.domain.organization.dto.GetOrgSeniorsResponse;
import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.organization.repository.OrganizationRepository;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.senior.repository.SeniorRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.ENTITY_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class GetOrgSeniorsService {
    private final OrganizationRepository organizationRepository;
    private final SeniorRepository seniorRepository;
    private final MatchingRepository matchingRepository;
    public GetOrgSeniorsResponse getSeniors(Long orgUserId) {
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
}
