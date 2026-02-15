package com.kay.review.repository;

import com.kay.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("select r from Review r where r.companyId = ?1")
    List<Review> findAllByCompanyId(Long companyId);

    boolean existsByTitleIgnoreCaseAndCompanyId(String title, Long companyId);

    Optional<Review> findByReviewIdAndCompanyId(Long reviewId, Long companyId);

    @Modifying
    @Query("delete  from Review r where r.reviewId = ?1 AND r.companyId = ?2")
    void deleteByReviewIdAndCompanyId(Long reviewId, Long companyId);
}
