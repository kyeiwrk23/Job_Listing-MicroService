package com.kay.submitApplication.dto;

import lombok.Data;

@Data
public class ApplicationStatisticsDto {
    private int total;
    private int submitted;
    private int shortListed;
    private int rejected;
}
