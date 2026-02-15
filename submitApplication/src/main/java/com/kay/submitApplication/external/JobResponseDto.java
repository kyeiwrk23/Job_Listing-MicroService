package com.kay.submitApplication.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponseDto {
    private Long jobId;
    private String title;
    private  String description;
    private String location;
    private String minSalary;
    private String maxSalary;
    private String contractType;
    private Long companyId;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
}
