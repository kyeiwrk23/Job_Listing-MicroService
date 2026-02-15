package com.kay.submitApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusResponseDto {
    private Long applicationId;
    private String status;
    private LocalDateTime updatedAt;
}
