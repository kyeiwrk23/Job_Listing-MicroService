package com.kay.review.dto;

import lombok.Data;

@Data
public class UpdateReviewDto {
    private String title;
    private String description;
    private double rating;
    private Long companyId;
}
