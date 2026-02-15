package com.kay.jobs.model;

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
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @NotBlank(message = "Title can't be Blank")
    @Size(message = "Title  shouldn't be less than 2 alphabets", min = 2)
    private String title;

    @NotBlank(message = "Description can't be Blank")
    @Size(message = "Description  shouldn't be less than 10 alphabets", min = 10)
    private  String description;

    @NotBlank(message = "Location can't be Blank")
    @Size(message = "Location  shouldn't be less than 2 alphabets", min = 2)
    private String location;

    @NotBlank(message = "Minimum Salary can't be Blank")
    @Size(message = "Minimum Salary shouldn't be less than 3 digits", min = 3)
    private String minSalary;

    @NotBlank(message = "Maximum Salary can't be Blank")
    @Size(message = "Maximum Salary shouldn't be less than 3 digits", min = 3)
    private String maxSalary;

    @NotNull(message = "companyId can't be Null")
    private Long companyId;

    @NotBlank(message = "Contract Type can't be Blank")
    @Size(message = "Contract Type shouldn't be less than 5", min = 5)
    private String contractType;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModified;


}
