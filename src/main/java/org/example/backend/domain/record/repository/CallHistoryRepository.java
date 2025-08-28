package org.example.backend.domain.record.repository;

import org.example.backend.domain.record.model.CallHistory;
import org.example.backend.domain.record.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallHistoryRepository extends JpaRepository<CallHistory, Long> {

}
