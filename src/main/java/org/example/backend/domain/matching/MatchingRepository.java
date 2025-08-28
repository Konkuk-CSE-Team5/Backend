package org.example.backend.domain.matching;

import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.matching.model.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
    Optional<Matching> findByVolunteerIdAndSeniorIdAndMatchingStatus(Long volunteerId, Long seniorId, MatchingStatus status);
}
