package com.rds.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 120)
    private String name;

    @Lob
    @Column(name = "phone_encrypted")
    private byte[] phoneEncrypted;

    @Lob
    @Column(name = "address_encrypted")
    private byte[] addressEncrypted;

    @Column(nullable = false)
    private Instant createdAt;

    @PrePersist
    void pre() {
        if (createdAt == null) createdAt = Instant.now();
    }
}
