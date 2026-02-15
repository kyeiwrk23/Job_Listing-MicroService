package com.kay.submitApplication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class SubmitApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @NotNull(message = "Job Id can't be null")
    private Long jobId;

    @NotNull(message = "Applicant/user Id can't be null")
    private String applicantId;

    @NotBlank(message = "Resume URL can't be Blank")
    @Size(min = 7, message = "Resume url should be more than 7 characters")
    private String resumeUrl;

    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @CreatedDate
    private LocalDateTime appliedAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
