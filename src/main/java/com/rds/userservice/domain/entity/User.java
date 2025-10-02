package com.rds.userservice.domain.entity;

import com.rds.securitylib.constant.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Table(name = "users", schema = "user_service")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "phone")
    private byte[] phone;

    @Column(name = "address")
    private byte[] address;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(name = "role")
    private RoleEnum role;

    @PrePersist
    void pre() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
