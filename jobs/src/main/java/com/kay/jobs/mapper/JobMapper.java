package com.kay.jobs.mapper;

import com.kay.jobs.dto.CompanyReviewDto;
import com.kay.jobs.dto.JobDto;
import com.kay.jobs.dto.JobResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobMapper {

    public static JobDto jobMapper(JobResponseDto  jobResponseDto, CompanyReviewDto companyReviewDto){
        JobDto jobDto = new JobDto();
        jobDto.setTitle(jobResponseDto.getTitle());
        jobDto.setDescription(jobResponseDto.getDescription());
        jobDto.setLocation(jobResponseDto.getLocation());
        jobDto.setMinSalary(jobResponseDto.getMinSalary());
        jobDto.setMaxSalary(jobResponseDto.getMaxSalary());
        jobDto.setContractType(jobResponseDto.getContractType());
        jobDto.setJobId(jobResponseDto.getJobId());
        jobDto.setCompany(companyReviewDto.getCompany());
        jobDto.setReview(companyReviewDto.getReviews());

        return jobDto;
    }
}
