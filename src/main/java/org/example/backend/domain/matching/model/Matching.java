package org.example.backend.domain.matching.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.global.common.model.BaseEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Matching extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MatchingStatus matchingStatus = MatchingStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "senior_id", nullable = false)
    private Senior senior;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;


    public void updateVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

}