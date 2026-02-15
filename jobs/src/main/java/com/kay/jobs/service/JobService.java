package com.kay.jobs.service;

import com.kay.jobs.dto.JobRequestDto;
import com.kay.jobs.dto.JobResponseDto;
import com.kay.jobs.dto.PaginatedJobResponse;

import java.util.List;

public interface JobService {
    PaginatedJobResponse findAll(String pageNumber, String pageSize, String sortBy, String sortOrder);
    JobResponseDto jobById(Long id);
    String createJob(JobRequestDto requestDto);
    String deleteJob(Long id);
    JobResponseDto updateJob(Long id, JobRequestDto requestDto);
    String JobByIdByCompany(Long jobId);
    PaginatedJobResponse jobsPublishedByCompany(Long CompanyId,String pageNumber, String pageSize, String sortBy, String sortOrder);

    List<JobResponseDto> searchJobs(String title,String Location,String jobType);


}
