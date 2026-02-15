package com.kay.submitApplication.dto;


import com.kay.submitApplication.model.StatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationResponseDto {
    private Long applicationId;
    private Long jobId;
    private String applicantId;
    private String resumeUrl;
    private String coverLetter;
    private StatusEnum status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
