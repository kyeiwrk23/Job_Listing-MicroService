package com.kay.jobs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestDto {
    private String title;
    private  String description;
    private String location;
    private String minSalary;
    private String maxSalary;
    private String contractType;
    private Long companyId;
}
