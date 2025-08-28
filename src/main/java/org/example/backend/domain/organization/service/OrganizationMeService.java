package org.example.backend.domain.organization.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.organization.dto.GetOrganizationMeResponse;
import org.example.backend.domain.organization.dto.PatchOrganizationMeRequest;
import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.organization.repository.OrganizationRepository;
import org.example.backend.domain.users.model.User;
import org.example.backend.domain.users.repository.UserRepository;
import org.example.backend.domain.volunteer.dto.GetVolunteerMeResponse;
import org.example.backend.domain.volunteer.dto.PatchVolunteerMeRequest;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.domain.volunteer.repository.VolunteerRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrganizationMeService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public GetOrganizationMeResponse get(Long loginUserId) {

        // 봉사자 정보 조회
        User user = userRepository.findById(loginUserId).orElseThrow(() -> new CustomException(BAD_REQUEST));
        Organization organization = organizationRepository.findByUser(user).orElseThrow(() -> new CustomException(BAD_REQUEST));

        GetOrganizationMeResponse.ProfileGetDto profileGetDto = GetOrganizationMeResponse.ProfileGetDto.builder()
                .userId(user.getUsername())
                .name(organization.getName())
                .manager(organization.getManager())
                .managerContact(organization.getManagerPhone())
                .build();

        return GetOrganizationMeResponse.builder()
                .profile(profileGetDto)
                .build();

    }

    @Transactional
    public void patch(Long loginUserId, PatchOrganizationMeRequest request) {
        // 봉사자 정보 조회
        User user = userRepository.findById(loginUserId).orElseThrow(() -> new CustomException(BAD_REQUEST));
        Organization organization = organizationRepository.findByUser(user).orElseThrow(() -> new CustomException(BAD_REQUEST));

        // 봉사자 정보 수정
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(request.password());
        }

        if (request.name() != null && !request.name().isBlank()) {
            organization.setName(request.name());
        }
        if (request.manager() != null && !request.manager().isBlank()) {
            organization.setManager(request.manager());
        }
        if (request.managerContact() != null && !request.managerContact().isBlank()) {
            organization.setManagerPhone(request.managerContact());
        }
        userRepository.save(user);
        organizationRepository.save(organization);

    }
}
