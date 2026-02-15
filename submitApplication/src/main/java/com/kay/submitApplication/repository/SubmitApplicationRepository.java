package com.kay.submitApplication.repository;


import com.kay.submitApplication.model.SubmitApp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmitApplicationRepository extends JpaRepository<SubmitApp,Long> {
    Optional<SubmitApp> findByApplicantIdAndJobId(String applicantId, Long jobId);
    Page<SubmitApp> findAllByJobId(Long jobId, Pageable pageable);

    Page<SubmitApp> findApplicationsByApplicantId(String applicantId, Pageable pageable);

    @Query("select j from SubmitApp j where j.jobId = ?1")
    List<SubmitApp> findAllJobs(Long jobId);
}
