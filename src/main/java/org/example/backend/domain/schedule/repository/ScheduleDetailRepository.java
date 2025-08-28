package org.example.backend.domain.schedule.repository;

import org.example.backend.domain.schedule.model.Schedule;
import org.example.backend.domain.schedule.model.ScheduleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleDetailRepository extends JpaRepository<ScheduleDetail, Long> {
    List<ScheduleDetail> findAllBySchedule(Schedule schedule);
    List<ScheduleDetail> findAllByScheduleIn(List<Schedule> schedules);
}
