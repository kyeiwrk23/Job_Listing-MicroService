package com.kay.jobs.external;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyResponseDto {
    private Long companyId;
    private String name;
    private String description;
    private String website;
    private String userId;
    private String email;
    private Double rating;
    private String location;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;
}
