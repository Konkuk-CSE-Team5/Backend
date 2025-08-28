package org.example.backend.domain.record.repository;

import org.example.backend.domain.record.model.VolunteerRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRecordRepository extends JpaRepository<VolunteerRecord, Long> {
}
