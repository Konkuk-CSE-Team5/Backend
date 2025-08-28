package org.example.backend.domain.record.repository;

import org.example.backend.domain.record.model.Report;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByVolunteerRecord(VolunteerRecord volunteerRecord);
}
