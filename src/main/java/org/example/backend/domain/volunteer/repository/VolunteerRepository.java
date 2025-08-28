package org.example.backend.domain.volunteer.repository;

import org.example.backend.domain.volunteer.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
}
