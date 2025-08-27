package org.example.backend.global.auth.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.domain.users.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user ->{
                    log.info("[loadUserByUsername] User was found. user = {}",  user);
                    return new CustomUserDetails(user);
                })
                .orElseThrow(() -> {
                    log.info("[loadUserByUsername] User with email {} cannot found!!", username );
                    throw new UsernameNotFoundException("User with email " + username + " cannot found!!");
                });
    }
}
