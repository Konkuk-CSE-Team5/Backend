package org.example.backend.domain.volunteer.dto;

import java.time.LocalDate;

public record RegisterVolunteerRequest(
    String username,
    String password,
    String name,
    LocalDate birthday,
    String gender,
    String contact
) {
}
