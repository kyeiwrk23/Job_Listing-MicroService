package com.kay.company.model;

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
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @NotBlank(message = "Name of Company can't be blank")
    @Size(message = "Name of Company can't be less than 2", min = 2)
    private String name;

    @NotBlank(message = "Company Description can't be blank")
    @Size(message = "Company Description can't be less than5")
    private String description;

    @NotBlank(message = "website can't be blank")
    @Size(message = "website entry can't be less than 6", min = 6)
    private String website;

    @NotBlank(message = "User id Can't be blank")
    private String userId;

    @Email
    private String email;

    @NotBlank(message = "Location can't be blank")
    @Size(message = "Location entry can't be less than 2", min = 2)
    private String location;

    private Double rating;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModified;
}
