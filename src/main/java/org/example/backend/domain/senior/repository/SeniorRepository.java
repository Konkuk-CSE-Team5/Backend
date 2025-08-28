package org.example.backend.domain.senior.repository;

import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.senior.model.Senior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeniorRepository extends JpaRepository<Senior, Long> {
    List<Senior> findAllByOrganization(Organization organization);
}
