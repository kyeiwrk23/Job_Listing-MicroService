package com.kay.review.dto;

import lombok.Data;

@Data
public class ReviewRequestDto {
    private String title;
    private String description;
    private double rating;
}
