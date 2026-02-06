package com.arimar.gwent.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "gw_user")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "game_id")
    private String gameId;

    @Column(name = "user_tag")
    private String tag;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (userId == null) {
            userId = UUID.randomUUID();
        }
        if (createdAt == null){
            createdAt = Instant.now();
        }
    }
}
