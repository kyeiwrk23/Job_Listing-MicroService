package com.kay.review.service;

import com.kay.review.dto.ReviewRequestDto;
import com.kay.review.dto.ReviewResponseDto;
import com.kay.review.dto.UpdateReviewDto;

import java.util.List;

public interface ReviewService {

    List<ReviewResponseDto> reviewsByCompanyId(Long companyId);
    ReviewResponseDto addReview(Long CompanyId, ReviewRequestDto requestDto);
    ReviewResponseDto getReview(Long reviewId);
    ReviewResponseDto updateReview(Long reviewId, UpdateReviewDto requestDto);
    boolean deleteReview(Long reviewId);

}
