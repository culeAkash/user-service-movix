package com.movix.user.service.entities;

import com.movix.user.service.enums.Active;
import com.movix.user.service.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(nullable = false,name = "first_name")
    private String firstName;

    @Column(nullable = false,name = "last_name")
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;


    @Column(unique = true,nullable = false,name = "email_id")
    private String emailId;

    @Column(nullable = false)
    private String password;

    private String aboutMe;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Active active;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
