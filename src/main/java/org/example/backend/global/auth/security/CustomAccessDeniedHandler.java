package org.example.backend.global.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.global.common.response.BaseErrorResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.example.backend.global.common.response.status.BaseExceptionResponseStatus.FORBIDDEN;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        BaseErrorResponse body =  new BaseErrorResponse(FORBIDDEN);
        String json = objectMapper.writeValueAsString(body);

        response.setStatus(FORBIDDEN.getCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(json);
    }
}