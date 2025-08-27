package org.example.backend.global.config;

import lombok.RequiredArgsConstructor;
import org.example.backend.global.auth.filter.JwtAuthenticationFilter;
import org.example.backend.global.auth.security.CustomAccessDeniedHandler;
import org.example.backend.global.auth.security.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final String[] PERMIT_URL = {
            "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html", "/swagger-ui/index.html",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf((auth) -> auth.disable());

        http
                .formLogin((auth)->auth.disable());

        http
                .httpBasic((auth)->auth.disable());

        // 토큰 기반 인증 비활성화
        http
                .authorizeHttpRequests((auth)-> auth
                        .anyRequest().permitAll());

        // 토큰 기반 인증 활성화
//        http
//                .authorizeHttpRequests((auth)-> auth
//                        .requestMatchers(PERMIT_URL).permitAll()
//                        .requestMatchers("test").hasRole("ORG")
//                        .anyRequest().authenticated());

        // 토큰 검증 필터 추가
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 토큰 검증 예외 처리 추가
        http
                .exceptionHandling(
                        configurer -> configurer.authenticationEntryPoint(customAuthenticationEntryPoint)
                                        .accessDeniedHandler(customAccessDeniedHandler));

        http
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
