package com.kay.submitApplication.dto;
import lombok.Data;


@Data
public class SubmitApplicationRequestDto {
    private Long jobId;
    private String applicantId;
    private String resumeUrl;
    private String coverLetter;
    private String status;
}
