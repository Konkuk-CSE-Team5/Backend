package org.example.backend.domain.record.repository;

import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.record.model.Report;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
