package org.example.backend.domain.organization.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.organization.dto.RegisterOrganizationRequest;
import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.organization.repository.OrganizationRepository;
import org.example.backend.domain.users.model.Role;
import org.example.backend.domain.users.model.User;
import org.example.backend.domain.users.repository.UserRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.DUPLICATE_USERNAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterOrganizationService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public void register(RegisterOrganizationRequest request) {
        log.info("[register] request = {}", request);

        if(userRepository.existsByUsername(request.username())){
            throw new CustomException(DUPLICATE_USERNAME);
        }

        // 유저 저장하고
        User user = User.builder().username(request.username())
                .password(request.password())
                .role(Role.ORG)
                .build();
        userRepository.save(user);

        // 기관을 저장
        Organization organization = Organization.builder().name(request.name().trim())
                .manager(request.manager().trim())
                .managerPhone(request.managerContact().trim())
                .user(user)
                .build();
        organizationRepository.save(organization);
    }
}
