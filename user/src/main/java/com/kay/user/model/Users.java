package com.kay.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @NotBlank(message = "Username can't be blank")
    @Size(message = "Username must be at least 2 characters", min = 2)
    private String userName;

    @NotBlank(message = "firstName can't be blank")
    @Size(message = "firstName must be at least 2 characters", min = 2)
    private String firstName;

    @NotBlank(message = "lastName can't be blank")
    @Size(message = "lastName must be at least 2 characters", min = 2)
    private String lastName;

    @Email
    private String email;

    @NotBlank(message = "Phone number can't be blank")
    @Size(message = "Phone number must be at least 2 characters", min = 10)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.USER;

    private String keycloakId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModified;
}
