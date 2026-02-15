package com.kay.submitApplication.dto;

import com.kay.submitApplication.external.JobResponseDto;
import com.kay.submitApplication.external.UserResponseDto;
import com.kay.submitApplication.model.StatusEnum;
import lombok.Data;

@Data
public class SubmitApplicationDto {
    private Long applicationId;
    private String applicantId;
    private String resumeUrl;
    private String coverLetter;
    private StatusEnum status;
    private UserResponseDto applicant;
    private JobResponseDto job;
}
