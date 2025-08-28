package org.example.backend.domain.record.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.domain.schedule.model.ScheduleDetail;
import org.example.backend.global.common.model.BaseEntity;
import org.example.backend.global.convertor.DurationToSecondsConverter;

import java.time.Duration;
import java.time.LocalDate;
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
    private VolunteerRecordStatus volunteerRecordStatus;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    @Column(nullable = false)
    private LocalTime scheduledTime;

    private LocalDateTime startTime;

    @Convert(converter = DurationToSecondsConverter.class)
    private Duration totalCallTime;

    @OneToOne
    @JoinColumn(name = "schedule_detail_id")
    private ScheduleDetail scheduleDetail;

    @OneToOne(mappedBy = "volunteerRecord")
    private Report report;

    @ManyToOne
    @JoinColumn(name = "matching_id")
    private Matching matching;
}