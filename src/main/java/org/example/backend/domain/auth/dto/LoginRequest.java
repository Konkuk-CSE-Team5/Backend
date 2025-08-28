package org.example.backend.domain.auth.dto;

public record LoginRequest(
        String username,
        String password
) {
}
