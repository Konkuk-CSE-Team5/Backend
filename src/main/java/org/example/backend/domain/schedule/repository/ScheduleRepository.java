package org.example.backend.domain.schedule.repository;

import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByMatching(Matching matching);
    List<Schedule> findAllByMatching(Matching matching);
    List<Schedule> findAllByMatchingIn(List<Matching> matchings);
}
