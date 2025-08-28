package org.example.backend.domain.organization.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.backend.domain.users.model.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String manager;

    @Column(nullable = false)
    private String managerPhone;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
