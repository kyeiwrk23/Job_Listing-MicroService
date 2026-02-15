package com.kay.jobs.dto;

import com.kay.jobs.external.CompanyResponseDto;
import com.kay.jobs.external.ReviewResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class JobDto {
    private Long jobId;
    private String title;
    private  String description;
    private String location;
    private String minSalary;
    private String maxSalary;
    private String contractType;
    private CompanyResponseDto company;
    private List<ReviewResponseDto> review;
}
