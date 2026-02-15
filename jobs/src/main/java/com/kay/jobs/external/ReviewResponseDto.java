package com.kay.jobs.external;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String title;
    private String description;
    private double rating;
    private Long companyId;
}
