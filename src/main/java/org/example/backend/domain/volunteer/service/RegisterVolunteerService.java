package org.example.backend.domain.volunteer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.users.model.Role;
import org.example.backend.domain.users.model.User;
import org.example.backend.domain.users.repository.UserRepository;
import org.example.backend.domain.volunteer.dto.RegisterVolunteerRequest;
import org.example.backend.domain.volunteer.model.Gender;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.domain.volunteer.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterVolunteerService {
    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    @Transactional
    public void register(RegisterVolunteerRequest request) {
        log.info("[register] request = {}", request);

        // 유저 정보를 저장
        User user = User.builder().username(request.username())
                .password(request.password())
                .role(Role.VOL)
                .build();
        userRepository.save(user);

        // 봉사자 정보를 저장

        Volunteer volunteer = Volunteer.builder().birthday(request.birthday())
                .gender("male".equals(request.gender().trim()) ? Gender.MALE : Gender.FEMALE)
                .name(request.name().trim())
                .phone(request.contact().trim())
                .user(user)
                .build();

        volunteerRepository.save(volunteer);
    }
}
