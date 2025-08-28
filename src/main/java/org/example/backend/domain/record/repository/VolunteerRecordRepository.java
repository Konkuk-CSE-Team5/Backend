package org.example.backend.domain.record.repository;

import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerRecordRepository extends JpaRepository<VolunteerRecord, Long> {
    List<VolunteerRecord> findAllByMatchingOrderByIdDesc(Matching matching);
}
