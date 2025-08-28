package org.example.backend.domain.users.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.backend.global.common.model.BaseEntity;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
    }

    public boolean isSameRole(String role) {
        return this.role.name().equals(role);
    }
}
