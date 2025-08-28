package org.example.backend.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.domain.auth.dto.LoginRequest;
import org.example.backend.domain.auth.dto.LoginResponse;
import org.example.backend.domain.users.repository.UserRepository;
import org.example.backend.global.auth.util.JwtUtil;
import org.example.backend.global.common.exception.CustomException;
import org.springframework.stereotype.Service;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.LOGIN_FAILED;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    public LoginResponse login(LoginRequest request) {
        return userRepository.findByUsername(request.username()).map(user -> {
            if(!user.isSamePassword(request.password())){
                throw new CustomException(LOGIN_FAILED);
            }
            String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getId());
            return new LoginResponse(accessToken);
        }).orElseThrow(() -> new CustomException(LOGIN_FAILED));
    }
}
