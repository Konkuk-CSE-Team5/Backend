package org.example.backend.domain.record.repository;

import org.example.backend.domain.record.model.CallHistory;
import org.example.backend.domain.record.model.VolunteerRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CallHistoryRepository extends JpaRepository<CallHistory, Long> {
    List<CallHistory> findAllByVolunteerRecordOrderByStartTimeAsc(VolunteerRecord volunteerRecord);
    void deleteByVolunteerRecord(VolunteerRecord volunteerRecord);
}
