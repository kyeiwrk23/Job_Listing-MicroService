package com.kay.review.controller;

import com.kay.review.dto.ReviewRequestDto;
import com.kay.review.dto.ReviewResponseDto;
import com.kay.review.dto.UpdateReviewDto;
import com.kay.review.service.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RefreshScope
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private  final ReviewServiceImpl reviewService;


    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> reviewsByCompanyId(@RequestParam Long companyId){

        log.info("Reviews by company Id:{}",companyId);
        List<ReviewResponseDto> reviews = reviewService.reviewsByCompanyId(companyId);

        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> addReview(@RequestParam Long companyId,@RequestBody ReviewRequestDto requestDto)
    {
        ReviewResponseDto responseDto =  reviewService.addReview(companyId,requestDto);

        return new ResponseEntity<>(responseDto,HttpStatus.CREATED);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> reviewByCompanyAndReviewId(@PathVariable Long reviewId){
        ReviewResponseDto rResponse =  reviewService.getReview(reviewId);

        return new ResponseEntity<>(rResponse, HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(@PathVariable Long reviewId,
                                                          @RequestBody UpdateReviewDto requestDto){
        ReviewResponseDto reviewResponseDto =  reviewService.updateReview(reviewId,requestDto);

        return new ResponseEntity<>(reviewResponseDto,HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId){
        boolean message = reviewService.deleteReview(reviewId);

        if(message){
            return new ResponseEntity<>("Review Deleted Successfully",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Review not Deleted",HttpStatus.NOT_FOUND);
        }
    }

}
