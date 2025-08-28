package org.example.backend.domain.matching.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.global.common.model.BaseEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Matching extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchingStatus status = MatchingStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "senior_id", nullable = false)
    private Senior senior;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;
}