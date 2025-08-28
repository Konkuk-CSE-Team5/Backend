package org.example.backend.domain.volunteer.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.users.model.User;
import org.example.backend.domain.users.repository.UserRepository;
import org.example.backend.domain.volunteer.dto.GetVolunteerMeResponse;
import org.example.backend.domain.volunteer.dto.PatchVolunteerMeResponse;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.domain.volunteer.repository.VolunteerRepository;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;

@Slf4j
@RequiredArgsConstructor
@Service
public class VolunteerMeService {
    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    public GetVolunteerMeResponse get(Long loginUserId) {

        // 봉사자 정보 조회
        User user = userRepository.findById(loginUserId).orElseThrow(() -> new CustomException(BAD_REQUEST));
        Volunteer volunteer = volunteerRepository.findByUser(user).orElseThrow(() -> new CustomException(BAD_REQUEST));

        GetVolunteerMeResponse.ProfileGetDto profileGetDto = GetVolunteerMeResponse.ProfileGetDto.builder()
                .name(volunteer.getName())
                .phone(volunteer.getPhone())
                .build();

        return GetVolunteerMeResponse.builder()
                .profile(profileGetDto)
                .build();
    }

    @Transactional
    public void patch(Long loginUserId, PatchVolunteerMeResponse request) {
        // 봉사자 정보 조회
        User user = userRepository.findById(loginUserId).orElseThrow(() -> new CustomException(BAD_REQUEST));
        Volunteer volunteer = volunteerRepository.findByUser(user).orElseThrow(() -> new CustomException(BAD_REQUEST));

        // 봉사자 정보 수정
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(request.password());
        }
        if(request.name() != null && !request.name().isBlank()) {
            volunteer.setName(request.name());
        }
        if (request.birthday() != null) {
            volunteer.setBirthday(request.birthday());
        }
        if (request.gender() != null) {
            volunteer.setGender(request.gender());
        }
        if(request.contact() != null && !request.contact().isBlank()) {
            volunteer.setPhone(request.contact());
        }
    }
}
