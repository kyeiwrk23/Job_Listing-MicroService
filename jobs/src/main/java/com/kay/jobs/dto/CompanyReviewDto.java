package com.kay.jobs.dto;

import com.kay.jobs.external.CompanyResponseDto;
import com.kay.jobs.external.ReviewResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CompanyReviewDto {
    CompanyResponseDto company;
    List<ReviewResponseDto> reviews;
}
