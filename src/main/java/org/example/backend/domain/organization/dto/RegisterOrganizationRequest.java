package org.example.backend.domain.organization.dto;

public record RegisterOrganizationRequest(
        String username,
        String password,
        String name,
        String manager,
        String managerContact
) {
}
