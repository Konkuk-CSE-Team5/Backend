package org.example.backend.domain.organization.repository;

import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByUser(User user);

    Optional<Organization> findByUserId(Long userId);
}
