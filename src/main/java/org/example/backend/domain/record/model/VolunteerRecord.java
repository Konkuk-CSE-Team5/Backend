package org.example.backend.domain.record.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.domain.schedule.model.ScheduleDetail;
import org.example.backend.global.common.model.BaseEntity;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VolunteerRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VolunteerRecordStatus status;

    private LocalDateTime startTime;

    private LocalTime totalCallTime;

    @OneToOne
    @JoinColumn(name = "schedule_detail_id")
    private ScheduleDetail scheduleDetail;
}
