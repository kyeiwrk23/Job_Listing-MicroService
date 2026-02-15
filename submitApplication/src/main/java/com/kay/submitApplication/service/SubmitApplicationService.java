package com.kay.submitApplication.service;

import com.kay.submitApplication.dto.*;

public interface SubmitApplicationService {

    ApplicationResponseDto createApplication(SubmitApplicationRequestDto requestDto);
    SubmitApplicationDto applicationById(Long applicationId);
    PaginatedSubmitResponse findAllApplicationsPerJob(Long jobId, String pageNumber, String pageSize, String sortBy, String sortOrder);
    PaginatedSubmitResponse findAllApplicationsByApplicant(String applicantId,String pageNumber,String pageSize,String sortBy,String sortOrder);
    ApplicationStatusResponseDto updateStatus(Long applicationId, SubmitApplicationRequestDto statusDto);
    String withDrawApplication(Long applicationId);
    boolean checkIfApplied(Long jobId,String applicantId);
    ApplicationStatisticsDto appStatistics(Long jobId);
    String deleteApplication(Long applicationId);
}
