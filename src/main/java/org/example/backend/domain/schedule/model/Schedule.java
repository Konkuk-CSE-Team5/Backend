package org.example.backend.domain.schedule.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.domain.matching.model.Matching;
import org.example.backend.global.common.model.BaseEntity;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "matching_id", nullable = false)
    private Matching matching;
}
