package com.kay.review.dto;

import lombok.Data;

@Data
public class ReviewResponseDto {
    private Long reviewId;
    private String title;
    private String description;
    private double rating;
    private Long companyId;
}
