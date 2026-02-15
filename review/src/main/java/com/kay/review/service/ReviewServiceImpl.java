package com.kay.review.service;

import com.kay.review.dto.ReviewRateDto;
import com.kay.review.dto.ReviewRequestDto;
import com.kay.review.dto.ReviewResponseDto;
import com.kay.review.dto.UpdateReviewDto;
import com.kay.review.exception.NoReviewExistException;
import com.kay.review.model.Review;
import com.kay.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    private final StreamBridge streamBridge;

    @Override
    public List<ReviewResponseDto> reviewsByCompanyId(Long companyId) {
        List<Review> review = reviewRepository.findAllByCompanyId(companyId);
        if(review.isEmpty()){
            return null;
        }

        return review.stream()
                .map(rev -> modelMapper.map(rev,ReviewResponseDto.class))
                .toList();
    }


    @Override
    public ReviewResponseDto addReview(Long companyId, ReviewRequestDto requestDto) {

        Review review = modelMapper.map(requestDto,Review.class);
        review.setCompanyId(companyId);

        Review savedReview = reviewRepository.save(review);

        List<Review> reviews = reviewRepository.findAllByCompanyId(savedReview.getCompanyId());
        if(!reviews.isEmpty()){
            OptionalDouble rates = reviews
                    .stream()
                    .mapToDouble(Review::getRating)
                            .average();

            rates.ifPresent(
                    rate ->{
                        log.info("data to publish: {} : {}",rate,companyId);
                        Double rating = BigDecimal.valueOf(rate)
                                .setScale(1, RoundingMode.HALF_UP)
                                .doubleValue();
                        ReviewRateDto reviewRateDto = new ReviewRateDto(companyId,rating);
                        boolean sent = streamBridge.send("rate.exchange",reviewRateDto);
                        log.info("sent: {}",sent);
                    }
            );
        }

        return modelMapper.map(savedReview,ReviewResponseDto.class);
    }

    @Override
    public ReviewResponseDto getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                ()-> new NoReviewExistException("No Review exist with review Id " + reviewId));
        return modelMapper.map(review,ReviewResponseDto.class);
    }

    @Override
    public ReviewResponseDto updateReview(Long reviewId, UpdateReviewDto requestDto) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        review.ifPresent(
                rev -> {
                    rev.setTitle(requestDto.getTitle());
                    rev.setDescription(requestDto.getDescription());
                    rev.setRating(requestDto.getRating());
                    rev.setCompanyId(requestDto.getCompanyId());
                }
        );

        Review savedReview = reviewRepository.save(review.get());
        return modelMapper.map(savedReview,ReviewResponseDto.class);
    }

    @Override
    public boolean deleteReview(Long reviewId) {

        if(reviewRepository.existsById(reviewId)){
            reviewRepository.deleteById(reviewId);
            return true;
        }

        return false;
    }

}
