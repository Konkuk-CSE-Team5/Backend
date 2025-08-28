package org.example.backend.domain.organization.repository;

import org.example.backend.domain.organization.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
