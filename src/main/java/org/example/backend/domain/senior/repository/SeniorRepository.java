package org.example.backend.domain.senior.repository;

import org.example.backend.domain.organization.model.Organization;
import org.example.backend.domain.senior.model.Senior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeniorRepository extends JpaRepository<Senior, Long> {
    List<Senior> findAllByOrganization(Organization organization);

    Optional<Senior> findByCode(String code);
    boolean existsByCode(String code);
}
