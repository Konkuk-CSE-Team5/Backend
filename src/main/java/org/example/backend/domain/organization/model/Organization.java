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

    private String name;

    private String manager;

    private String managerPhone;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
