package org.example.backend.domain.matching.repository;

import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    Optional<Matching> findByVolunteerIdAndSeniorIdAndMatchingStatus(Long volunteerId, Long seniorId, MatchingStatus status);
    List<Matching> findAllByVolunteer(Volunteer volunteer);

    List<Matching> findAllBySeniorAndMatchingStatus(Senior senior, MatchingStatus matchingStatus);
}
