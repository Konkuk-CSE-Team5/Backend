package org.example.backend.domain.volunteer.repository;

import org.example.backend.domain.users.model.User;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findByUser(User user);

    Optional<Volunteer> findByUserId(Long userId);
}
