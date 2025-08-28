package org.example.backend.domain.matching.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.domain.senior.model.Senior;
import org.example.backend.domain.volunteer.model.Volunteer;
import org.example.backend.global.common.model.BaseEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Matching extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchingStatus matchingStatus = MatchingStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "senior_id", nullable = false)
    private Senior senior;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    public void updateMatchingStatus(MatchingStatus matchingStatus) {
        this.matchingStatus = matchingStatus;
    }

    public void updateVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    // 정적 팩토리 메소드들
    public static Matching createForSenior(Senior senior) {
        Matching matching = new Matching();
        matching.senior = senior;
        matching.matchingStatus = MatchingStatus.ACTIVE;
        matching.volunteer = null;
        return matching;
    }

    public static Matching createWithVolunteer(Senior senior, Volunteer volunteer) {
        Matching matching = new Matching();
        matching.senior = senior;
        matching.volunteer = volunteer;
        matching.matchingStatus = MatchingStatus.ACTIVE;
        return matching;
    }
}