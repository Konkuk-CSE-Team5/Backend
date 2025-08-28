package org.example.backend.domain.record.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.global.common.model.BaseEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HealthLevel health;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private mentalityLevel mentality;

    @Column(columnDefinition = "TEXT")
    private String opinion;

    @ManyToOne
    @JoinColumn(name = "volunteer_record_id")
    private VolunteerRecord volunteerRecord;
}