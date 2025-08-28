package org.example.backend.domain.record.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.global.common.model.BaseEntity;
import org.example.backend.global.convertor.DurationToSecondsConverter;

import java.time.Duration;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CallHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime startTime;

    @Convert(converter = DurationToSecondsConverter.class)
    private Duration callTime;

    @ManyToOne
    @JoinColumn(name = "volunteer_record_id")
    private VolunteerRecord volunteerRecord;
}
